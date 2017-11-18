package com.ote.cucumber;

import com.ote.domain.mock.SecretRepositoryMock;
import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.api.ServiceProvider;
import com.ote.domain.secret.business.NotFoundException;
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
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assumptions;

import java.util.stream.Collectors;

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

    @Given("I have created the group '(.*)'")
    public void groupHasBeenCreated(String name) throws Throwable {
        groupHasBeenCreated(ROOT_NAME, name);
    }

    @Given("I have created, under '(.*)', the group '(.*)'")
    public void groupHasBeenCreated(String parent, String name) throws Throwable {
        groupHasBeenDeclared(parent, name);
        createSecret(name);
    }

    @Given("I have created the secret '(.*)' with value '(.*)'")
    public void valueHasBeenCreated(String name, String value) throws Throwable {
        valueHasBeenCreated(ROOT_NAME, name, value);
    }

    @Given("I have created, under '(.*)', the secret '(.*)' with value '(.*)'")
    public void valueHasBeenCreated(String parent, String name, String value) throws Throwable {
        valueHasBeenDeclared(parent, name, value);
        createSecret(name);
    }

    @Given("I have declared the secret '(.*)' with value '(.*)'")
    public void valueHasBeenDeclared(String name, String value) {
        valueHasBeenDeclared(ROOT_NAME, name, value);
    }

    @Given("I have declared, under '(.*)', the secret '(.*)' with value '(.*)'")
    public void valueHasBeenDeclared(String parent, String name, String value) {
        SecretParam secretParam = new SecretParam();
        secretParam.setName(name);
        secretParam.setParent(parent);
        secretParam.setType(Type.VALUE);
        secretParam.setValue(value);
        context.put(name + PARAM_SUFFIX, secretParam);
    }

    @Given("I have declared the group '(.*)'")
    public void groupHasBeenDeclared(String name) {
        groupHasBeenDeclared(ROOT_NAME, name);
    }

    @Given("I have declared, under '(.*)', the group '(.*)'")
    public void groupHasBeenDeclared(String parent, String name) {
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

    @When("I remove the secret '(.*)'")
    public void removeSecret(String name) throws Throwable {

        ISecret secret = context.get(name + RESULT_SUFFIX, ISecret.class);
        secretService.remove(secret.getId());
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
        secret = secretService.find(secret.getId());

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

    @Then("the secret '(.*)' is removed")
    public void secretIsRemoved(String name) {
        ISecret secret = context.get(name + RESULT_SUFFIX, ISecret.class);
        Assertions.assertThatThrownBy(() -> secretService.find(secret.getId(), ISecret.class)).
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
        ISecret secret = context.get(name + RESULT_SUFFIX, ISecret.class);
        long secretId = secret.getId();
        try {
            secret = secretService.find(secretId); // could be null
        } catch (NotFoundException e) {
            secret = null;
        }
        IGroup parentGroup = context.get(parent + RESULT_SUFFIX, IGroup.class);
        parentGroup = (IGroup) secretService.find(parentGroup.getId());

        SoftAssertions softAssertions = new SoftAssertions();
        if (isChildOf) {
            if (secret != null) {
                softAssertions.assertThat(secret.getParent().getId()).isEqualTo(parentGroup.getId());
            }
            softAssertions.assertThat(parentGroup.getChildren().stream().map(ISecret::getId).collect(Collectors.toList())).contains(secretId);
        } else {
            if (secret != null) {
                softAssertions.assertThat(secret.getParent().getId()).isNotEqualTo(parentGroup.getId());
            }
            softAssertions.assertThat(parentGroup.getChildren().stream().map(ISecret::getId).collect(Collectors.toList())).doesNotContain(secretId);
        }
        softAssertions.assertAll();
    }


}
