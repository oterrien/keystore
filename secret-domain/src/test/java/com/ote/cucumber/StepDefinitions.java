package com.ote.cucumber;

import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.api.ServiceProvider;
import com.ote.domain.secret.api.model.Secret;
import com.ote.domain.spi.SecretRepositoryMock;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;

import java.util.List;
import java.util.UUID;

@Slf4j
public class StepDefinitions {

    private final SecretRepositoryMock secretRepository = new SecretRepositoryMock();
    private final ISecretService secretService = ServiceProvider.getInstance().createSecretService(secretRepository);
    private final ScenarioContext context = new ScenarioContext();

    @Before
    public void before(Scenario scenario) {
        log.trace("### Starting scenario: '" + scenario.getName() + "'");
    }

    @After
    public void after(Scenario scenario) {
        log.trace("### Finishing scenario: '" + scenario.getName() + "'");
        context.clear();
    }

    @Given("I have declared the following secret:")
    public void declareSecret(List<Field> fields) throws Throwable {
        String uid = UUID.randomUUID().toString();
        context.put("CURRENT", uid);
        context.put("SECRET_" + uid, new SecretBuilder(fields).build());
    }

    @When("I create this secret")
    public void createSecret() throws Throwable {
        String uid = context.get("CURRENT", String.class);
        Secret secret = context.get("SECRET_" + uid, Secret.class);
        secretService.create(secret);
    }

    @Then("this secret is create with correct information")
    public void secretIsCreated() throws Throwable {
        String uid = context.get("CURRENT", String.class);
        Secret secret = context.get("SECRET_" + uid, Secret.class);
        Secret secretReloaded = secretService.find(secret.getName());

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(secretReloaded.getName()).isEqualTo(secret.getName());
        assertions.assertThat(secretReloaded.getDescription()).isEqualTo(secret.getDescription());
        assertions.assertThat(secretReloaded.getValue()).isEqualTo(secret.getValue());
        assertions.assertAll();
    }


}
