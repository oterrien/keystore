package com.ote.cucumber;

import com.ote.SecretServiceApplication;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;
import com.ote.secret.rest.payload.GroupPayload;
import com.ote.secret.rest.payload.SecretPayload;
import com.ote.secret.rest.payload.SecretType;
import com.ote.secret.rest.payload.ValuePayload;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

@Slf4j
@WebAppConfiguration
@ContextConfiguration(classes = SecretServiceApplication.class)
public class StepDefinitions {

    private static final String ROOT_NAME = "Root";
    private static final String PARAM_SUFFIX = "_PARAM";
    private static final String RESULT_SUFFIX = "_RESULT";

    private ScenarioContext context = new ScenarioContext();

    @Autowired
    private ISecretRepository secretRepository;

    @Autowired
    private SecretRestControllerAdapter secretRestControllerAdapter;

    @Before
    public void before(Scenario scenario) {
        log.trace("### Starting scenario: '" + scenario.getName() + "'");
    }

    @After
    public void after(Scenario scenario) {
        log.trace("### Finishing scenario: '" + scenario.getName() + "'");
        context.clear();
        secretRepository.deleteAll();
    }

    @Given("I have created the group '" + ROOT_NAME + "'")
    public void rootHasBeenCreated() throws Throwable {
        groupHasBeenCreated(ROOT_NAME, null);
    }

    @Given("I have created the group '(.*)' under group '(.*)'")
    public void groupHasBeenCreated(String name, String parent) throws Throwable {
        groupHasBeenDeclared(name, parent);
        createSecret(name);
    }

    @Given("I have created the secret '(.*)' under group '(.*)' with value '(.*)'")
    public void valueHasBeenCreated(String name, String parent, String value) throws Throwable {
        valueHasBeenDeclared(name, parent, value);
        createSecret(name);
    }

    @Given("I have declared the secret '(.*)' under group '(.*)' with value '(.*)'")
    public void valueHasBeenDeclared(String name, String parent, String value) {
        ValuePayload payload = new ValuePayload();
        payload.setName(name);
        Optional.ofNullable(parent).ifPresent(p -> findGroup(p).ifPresent(pp -> payload.setParentId(pp.getId())));
        payload.setValue(value);
        context.put(name + PARAM_SUFFIX, payload);
    }

    @Given("I have declared the group '(.*)' under group '(.*)'")
    public void groupHasBeenDeclared(String name, String parent) {
        GroupPayload payload = new GroupPayload();
        payload.setName(name);
        Optional.ofNullable(parent).ifPresent(p -> findGroup(p).ifPresent(pp -> payload.setParentId(pp.getId())));
        context.put(name + PARAM_SUFFIX, payload);
    }

    @When("I create the secret '(.*)'")
    public void createSecret(String name) throws Throwable {
        SecretPayload payload = context.get(name + PARAM_SUFFIX, SecretPayload.class);
        create(payload);
    }

    @When("I move the secret '(.*)' to group '(.*)'")
    public void moveSecret(String name, String newParent) throws Throwable {
        SecretPayload secret = context.get(name + RESULT_SUFFIX, SecretPayload.class);
        SecretPayload parent = context.get(newParent + RESULT_SUFFIX, SecretPayload.class);
        secretRestControllerAdapter.changeParent(secret.getId(), parent.getId());
    }

    private Optional<GroupPayload> findGroup(String name) {
        return Optional.ofNullable(context.get(name + RESULT_SUFFIX, GroupPayload.class));
    }

    private void create(SecretPayload payload) throws Exception {
        long id = secretRestControllerAdapter.createSecret(payload);
        payload = secretRestControllerAdapter.findSecret(id);
        context.put(payload.getName() + RESULT_SUFFIX, payload);
    }

    @Then("the secret '(.*)' is created")
    public void secretIsCreated(String name) throws Throwable {

        SecretPayload param = context.get(name + PARAM_SUFFIX, SecretPayload.class);
        SecretPayload result = context.get(name + RESULT_SUFFIX, SecretPayload.class);
        result = secretRestControllerAdapter.findSecret(result.getId());

        Assumptions.assumeTrue(result != null);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(result.getId()).isPositive();
        if (result.getParentId() > 0) {
            SecretPayload parent = secretRestControllerAdapter.findSecret(result.getParentId());
            secretIsChildOf(result.getName(), parent.getName());
        }
        if (result.getType() == SecretType.VALUE) {
            softAssertions.assertThat(((ValuePayload) result).getValue()).isEqualTo(((ValuePayload) param).getValue());
        }
        softAssertions.assertAll();
    }

    @Then("the secret '(.*)' is child of '(.*)'")
    public void secretIsChildOf(String name, String parent) throws Throwable {

        SecretPayload secret = context.get(name + RESULT_SUFFIX, SecretPayload.class);
        secret = secretRestControllerAdapter.findSecret(secret.getId());
        GroupPayload parentGroup = context.get(parent + RESULT_SUFFIX, GroupPayload.class);
        parentGroup = (GroupPayload) secretRestControllerAdapter.findSecret(parentGroup.getId());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(secret.getParentId()).isEqualTo(parentGroup.getId());
        softAssertions.assertThat(parentGroup.getChildren()).contains(secret.getId());
        softAssertions.assertAll();
    }

    @Then("the secret '(.*)' is not child of '(.*)'")
    public void secretIsNotChildOf(String name, String parent) throws Throwable {
        SecretPayload secret = context.get(name + RESULT_SUFFIX, SecretPayload.class);
        secret = secretRestControllerAdapter.findSecret(secret.getId());
        GroupPayload parentGroup = context.get(parent + RESULT_SUFFIX, GroupPayload.class);
        parentGroup = (GroupPayload) secretRestControllerAdapter.findSecret(parentGroup.getId());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(secret.getParentId()).isNotEqualTo(parentGroup.getId());
        softAssertions.assertThat(parentGroup.getChildren()).doesNotContain(secret.getId());
        softAssertions.assertAll();
    }
}
