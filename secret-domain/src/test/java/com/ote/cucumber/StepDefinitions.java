package com.ote.cucumber;

import com.ote.domain.mock.SecretRepositoryMock;
import com.ote.domain.model.SecretFactory;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;
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

import java.util.List;

@Slf4j
public class StepDefinitions {

    private ScenarioContext context;

    private ISecretRepository secretRepository;

    private static final String ROOT_NAME = "Root";
    private static final String PARAM_SUFFIX = "_PARAM";
    private static final String RESULT_SUFFIX = "_RESULT";

    @Before
    public void before(Scenario scenario) {
        log.trace("### Starting scenario: '" + scenario.getName() + "'");
        context = new ScenarioContext();
        secretRepository = new SecretRepositoryMock();
    }

    @After
    public void after(Scenario scenario) {
        log.trace("### Finishing scenario: '" + scenario.getName() + "'");
    }

    @Given("Root is created")
    public void rootIsCreated() throws Throwable {
        ISecret root = SecretFactory.getInstance().createGroup(ROOT_NAME, null);
        long id = secretRepository.save(root);
        root.setId(id);
        context.put(root.getName() + RESULT_SUFFIX, root);
    }

    @Given("following secrets:")
    public void theFollowingValue(List<SecretParam> secrets) throws Throwable {
        secrets.stream().
                peek(secret -> log.trace("### Handling secret " + secret.getName())).
                peek(secret -> {
                    if (secret.getParent() == null) {
                        secret.setParent(ROOT_NAME);
                    }
                }).
                peek(secret -> {
                    SoftAssertions assertions = new SoftAssertions();
                    assertions.assertThat(secret.getType()).isNotNull();
                    assertions.assertThat(secret.getName()).isNotNull();
                    if (secret.getType() == Type.Value) {
                        assertions.assertThat(secret.getValue()).isNotNull();
                    }
                    assertions.assertAll();
                }).
                forEach(secret -> context.put(secret.getName() + PARAM_SUFFIX, secret));
    }

    @When("I create each secret")
    public void createEachSecret() throws Throwable {

        context.getManyByPredicate(entry -> entry.getKey().endsWith(PARAM_SUFFIX), SecretParam.class).
                stream().
                peek(secret -> log.trace("### Creating secret " + secret.getName())).
                map(secret -> create(secret)).
                forEach(secret -> {
                    long id = secretRepository.save(secret);
                    secret.setId(id);
                    context.put(secret.getName() + RESULT_SUFFIX, secret);
                });
    }

    private ISecret create(SecretParam secret) {
        ISecret parent = context.getManyByPredicate(entry -> entry.getKey().endsWith(RESULT_SUFFIX), ISecret.class).
                stream().
                filter(secr -> secret.getParent().equalsIgnoreCase(secr.getName())).
                findAny().
                orElseThrow(() -> new RuntimeException("Parent (name=" + secret.getParent() + ") not exist (or not yet created)"));
        return secret.getType().create(secret, (IGroup) parent);
    }

    @Then("each secret is created")
    public void thisSecretIsCreated() throws Throwable {

        context.getManyByPredicate(entry -> entry.getKey().endsWith(PARAM_SUFFIX), SecretParam.class).
                stream().
                peek(secret -> log.trace("### Checking secret " + secret.getName() + " is well created")).
                forEach(secret -> {
                    ISecret result = context.getOneByKey(secret.getName() + RESULT_SUFFIX, ISecret.class);
                    Assumptions.assumeTrue(result != null);
                    SoftAssertions softAssertions = new SoftAssertions();
                    softAssertions.assertThat(result.getId()).isPositive();
                    softAssertions.assertThat(result.getParent().getName()).isEqualTo(secret.getParent());
                    if (secret.getType() == Type.Value) {
                        softAssertions.assertThat(((IValue) result).getValue()).contains(secret.getValue());
                    }
                    softAssertions.assertAll();
                });
    }

    @Then("each secret belongs to parent's children")
    public void thisSecretBelongsToRootSChildren() throws Throwable {
        context.getManyByPredicate(entry -> entry.getKey().endsWith("_PARAM"), SecretParam.class).
                stream().
                peek(secret -> log.trace("### Checking secret " + secret.getName() + " belongs to " + secret.getParent() + ".children")).
                forEach(secret -> {
                    ISecret result = context.getOneByKey(secret.getName() + "_RESULT", ISecret.class);
                    Assumptions.assumeTrue(result != null);
                    Assertions.assertThat(result.getParent().getChildren()).contains(result);
                });

    }
}
