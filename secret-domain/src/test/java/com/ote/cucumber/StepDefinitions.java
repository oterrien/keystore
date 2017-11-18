package com.ote.cucumber;

import com.ote.domain.mock.SecretRepositoryMock;
import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.api.ServiceProvider;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.IValue;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assumptions;

@Slf4j
public class StepDefinitions {

    private static final String ROOT_NAME = "Root";
    private static final String PARAM_SUFFIX = "_PARAM";
    private static final String RESULT_SUFFIX = "_RESULT";

    private SecretRepositoryMock secretRepository = new SecretRepositoryMock();
    private ScenarioContext context = new ScenarioContext();
    private ISecretService secretService = ServiceProvider.getInstance().createSecretService(secretRepository);

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
        SecretParam secretParam = new SecretParam();
        secretParam.setName(name);
        secretParam.setParent(parent);
        secretParam.setType(Type.VALUE);
        secretParam.setValue(value);
        context.put(name + PARAM_SUFFIX, secretParam);
    }

    @Given("I have declared the group '(.*)' under group '(.*)'")
    public void groupHasBeenDeclared(String name, String parent) {
        SecretParam secretParam = new SecretParam();
        secretParam.setName(name);
        secretParam.setParent(parent);
        secretParam.setType(Type.GROUP);
        context.put(name + PARAM_SUFFIX, secretParam);
    }

    @When("I create the secret '(.*)'")
    public void createSecret(String name) throws Throwable {
        SecretParam secretParam = context.get(name + PARAM_SUFFIX, SecretParam.class);
        create(secretParam);
    }

    @When("I move the secret '(.*)' to group '(.*)'")
    public void moveSecret(String name, String newParent) throws Throwable {

        ISecret secret = context.get(name + RESULT_SUFFIX, ISecret.class);
        IGroup parentGroup = context.get(newParent + RESULT_SUFFIX, IGroup.class);
        secretService.move(secret, parentGroup);
    }

    private IGroup findGroup(String name) {
        return context.get(name + RESULT_SUFFIX, IGroup.class);
    }

    private void create(SecretParam secretParam) {
        IGroup parent = findGroup(secretParam.getParent());
        ISecret secret = secretParam.getType().create(secretParam, parent);
        long id = secretService.create(secret);
        secret.setId(id);
        context.put(secretParam.getName() + RESULT_SUFFIX, secret);
    }

    @Then("the secret '(.*)' is created")
    public void secretIsCreated(String name) throws Throwable {

        SecretParam secretParam = context.get(name + PARAM_SUFFIX, SecretParam.class);
        ISecret secret = context.get(name + RESULT_SUFFIX, ISecret.class);

        Assumptions.assumeTrue(secret != null);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(secret.getId()).isPositive();
        if (secret.getParent() != null) {
            secretIsChildOf(secret.getName(), secret.getParent().getName());
        }
        if (secretParam.getType() == Type.VALUE) {
            softAssertions.assertThat(((IValue) secret).getValue()).isEqualTo(secretParam.getValue());
        }
        softAssertions.assertAll();
    }

    @Then("the secret '(.*)' is child of '(.*)'")
    public void secretIsChildOf(String name, String parent) throws Throwable {
        ISecret secret = context.get(name + RESULT_SUFFIX, ISecret.class);
        IGroup parentGroup = context.get(parent + RESULT_SUFFIX, IGroup.class);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(secret.getParent()).isEqualTo(parentGroup);
        softAssertions.assertThat(parentGroup.getChildren()).contains(secret);
        softAssertions.assertAll();
    }

    @Then("the secret '(.*)' is not child of '(.*)'")
    public void secretIsNotChildOf(String name, String parent) throws Throwable {
        ISecret secret = context.get(name + RESULT_SUFFIX, ISecret.class);
        IGroup parentGroup = context.get(parent + RESULT_SUFFIX, IGroup.class);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(secret.getParent()).isNotEqualTo(parentGroup);
        softAssertions.assertThat(parentGroup.getChildren()).doesNotContain(secret);
        softAssertions.assertAll();
    }
}
