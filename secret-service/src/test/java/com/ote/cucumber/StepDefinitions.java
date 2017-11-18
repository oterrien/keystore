package com.ote.cucumber;

import com.ote.SecretServiceApplication;
import com.ote.domain.secret.business.NotFoundException;
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
import org.assertj.core.api.Assertions;
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

    @Given("I have created the group '(.*)'")
    public void groupHasBeenCreatedWithRootByDefault(String name) throws Throwable {
        groupHasBeenCreated(ROOT_NAME, name);
    }

    @Given("I have created, under '(.*)', the group '(.*)'")
    public void groupHasBeenCreated(String parent, String name) throws Throwable {
        groupHasBeenDeclared(parent, name);
        createSecret(name);
    }

    @Given("I have created the secret '(.*)' with value '(.*)'")
    public void valueHasBeenCreatedWithRootByDefault(String name, String value) throws Throwable {
        valueHasBeenCreated(ROOT_NAME, name, value);
    }

    @Given("I have created, under '(.*)', the secret '(.*)' with value '(.*)'")
    public void valueHasBeenCreated(String parent, String name, String value) throws Throwable {
        valueHasBeenDeclared(parent, name, value);
        createSecret(name);
    }

    @Given("I have declared the secret '(.*)' with value '(.*)'$")
    public void valueHasBeenDeclaredWithRootByDefault(String name, String value) {
        valueHasBeenDeclared(ROOT_NAME, name, value);
    }

    @Given("I have declared, under '(.*)', the secret '(.*)' with value '(.*)'")
    public void valueHasBeenDeclared(String parent, String name, String value) {
        ValuePayload payload = new ValuePayload();
        payload.setName(name);
        Optional.ofNullable(parent).ifPresent(p -> findGroup(p).ifPresent(pp -> payload.setParentId(pp.getId())));
        payload.setValue(value);
        context.put(name + PARAM_SUFFIX, payload);
    }

    @Given("I have declared the group '(.*)'")
    public void groupHasBeenDeclaredWithRootByDefault(String name) {
        groupHasBeenDeclared(ROOT_NAME, name);
    }

    @Given("I have declared, under '(.*)', the group '(.*)'")
    public void groupHasBeenDeclared(String parent, String name) {
        GroupPayload payload = new GroupPayload();
        payload.setName(name);
        Optional.ofNullable(parent).ifPresent(p -> findGroup(p).ifPresent(pp -> payload.setParentId(pp.getId())));
        context.put(name + PARAM_SUFFIX, payload);
    }

    @When("I create the secret '(.*)'")
    public void createSecret(String name) throws Throwable {
        SecretPayload payload = context.get(name + PARAM_SUFFIX, SecretPayload.class);
        long id = secretRestControllerAdapter.createSecret(payload);
        payload = secretRestControllerAdapter.findSecret(id);
        context.put(payload.getName() + RESULT_SUFFIX, payload);
    }

    @When("I move the secret '(.*)' to group '(.*)'")
    public void moveSecret(String name, String newParent) throws Throwable {
        SecretPayload secret = context.get(name + RESULT_SUFFIX, SecretPayload.class);
        SecretPayload parent = context.get(newParent + RESULT_SUFFIX, SecretPayload.class);
        secretRestControllerAdapter.changeParent(secret.getId(), parent.getId());
    }

    @When("I remove the secret '(.*)'")
    public void removeSecret(String name) throws Throwable {
        SecretPayload secret = context.get(name + RESULT_SUFFIX, SecretPayload.class);
        secretRestControllerAdapter.deleteSecret(secret.getId());
    }

    private Optional<GroupPayload> findGroup(String name) {
        return Optional.ofNullable(context.get(name + RESULT_SUFFIX, GroupPayload.class)).map(p -> {
            try {
                return (GroupPayload) secretRestControllerAdapter.findSecret(p.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
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

    @Then("the secret '(.*)' is removed")
    public void secretIsRemoved(String name) {
        SecretPayload result = context.get(name + RESULT_SUFFIX, SecretPayload.class);
        Assertions.assertThatThrownBy(() -> secretRestControllerAdapter.findSecret(result.getId())).
                isInstanceOf(NotFoundException.class);
    }

    @Then("the secret '(.*)' is child of '(.*)'")
    public void secretIsChildOf(String name, String parent) throws Throwable {
        secretChildOf(name, parent, true);
    }

    @Then("the secret '(.*)' is not child of '(.*)'")
    public void secretIsNotChildOf(String name, String parent) throws Throwable {
        secretChildOf(name, parent, false);
    }

    @Then("the group '(.*)' should contain '(.*)'")
    public void groupIsParentOf(String parent, String name) throws Throwable {
        secretChildOf(name, parent, true);
    }

    @Then("the group '(.*)' should not contain '(.*)'")
    public void groupIsNotParentOf(String parent, String name) throws Throwable {
        secretChildOf(name, parent, false);
    }

    private void secretChildOf(String name, String parent, boolean isChildOf) throws Throwable {
        SecretPayload secret = context.get(name + RESULT_SUFFIX, SecretPayload.class);
        long secretId = secret.getId();
        try {
            secret = secretRestControllerAdapter.findSecret(secretId); // could be null
        } catch (NotFoundException e) {
            secret = null;
        }
        GroupPayload parentGroup = context.get(parent + RESULT_SUFFIX, GroupPayload.class);
        parentGroup = (GroupPayload) secretRestControllerAdapter.findSecret(parentGroup.getId());

        SoftAssertions softAssertions = new SoftAssertions();
        if (isChildOf) {
            if (secret != null) {
                softAssertions.assertThat(secret.getParentId()).isEqualTo(parentGroup.getId());
            }
            softAssertions.assertThat(parentGroup.getChildren()).contains(secretId);
        } else {
            if (secret != null) {
                softAssertions.assertThat(secret.getParentId()).isNotEqualTo(parentGroup.getId());
            }
            softAssertions.assertThat(parentGroup.getChildren()).doesNotContain(secretId);
        }
        softAssertions.assertAll();
    }
}
