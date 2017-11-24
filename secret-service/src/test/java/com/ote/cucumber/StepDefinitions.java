package com.ote.cucumber;

import com.ote.SecretServiceApplication;
import com.ote.domain.secret.api.model.Secret;
import com.ote.secret.rest.SecretPayloadMapperService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.UUID;

@Slf4j
@WebAppConfiguration
@ContextConfiguration(classes = SecretServiceApplication.class)
public class StepDefinitions {

    private final ScenarioContext context = new ScenarioContext();

    @Autowired
    private SecretPayloadMapperService secretPayloadMapperService;

    @Autowired
    private SecretRestControllerAdapter secretRestControllerAdapter;

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
        secretRestControllerAdapter.createSecret(secretPayloadMapperService.convert(secret));
    }

    @Then("this secret is created with correct information")
    public void secretIsCreated() throws Throwable {
        String uid = context.get("CURRENT", String.class);
        Secret secret = context.get("SECRET_" + uid, Secret.class);
        Secret secretReloaded = secretPayloadMapperService.convert(secretRestControllerAdapter.findSecret(secret.getName()));

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(secretReloaded.getName()).isEqualTo(secret.getName());
        assertions.assertThat(secretReloaded.getDescription()).isEqualTo(secret.getDescription());
        assertions.assertThat(secretReloaded.getValue()).isEqualTo(secret.getValue());
        assertions.assertAll();
    }
}
