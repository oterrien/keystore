package com.ote.domain;

import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.business.SecretService;
import com.ote.domain.secret.business.model.Group;
import com.ote.domain.secret.business.model.Value;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

import java.util.Optional;

@Tag("unit")
@Slf4j
public class SecretServiceTest {

    private ISecretRepository secretRepository;

    private Group root;

    @BeforeEach
    public void setUp() {
        secretRepository = new SecretRepositoryMock();
        root = new Group();
        root.setName("root");
        secretRepository.create(root);
    }

    @Test
    @DisplayName("a user should be able to create a secret value")
    public void aUserShouldBeAbleToCreateASecretValue(TestInfo testInfo) {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        ISecretService secretService = new SecretService(this.secretRepository);
        long id = secretService.createValue("myValue", "password", root);

        Optional<ISecret> secret = secretRepository.find(id);

        log.debug("Value : " + secret.get());

        Assertions.assertThat(secret).isPresent();
        Assumptions.assumeTrue(secret.isPresent());

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(secret.get()).isExactlyInstanceOf(Value.class);
        assertions.assertThat(secret.get().getName()).isEqualTo("myValue");
        assertions.assertThat(secret.get().getParent()).isEqualTo(root);
        assertions.assertThat(secret.get().getParent().getChildren()).contains(secret.get());
        assertions.assertThat(((Value) secret.get()).getValue()).isEqualTo("password");
        assertions.assertAll();
    }


    @Test
    @DisplayName("a user should be able to create an empty group")
    public void aUserShouldBeAbleToCreateAnEmptyGroup(TestInfo testInfo) {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        ISecretService secretService = new SecretService(this.secretRepository);
        long id = secretService.createGroup("myGroup", root);

        Optional<ISecret> secret = secretRepository.find(id);

        Assertions.assertThat(secret).isPresent();
        Assumptions.assumeTrue(secret.isPresent());

        log.debug("Group : " + secret.get());

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(secret.get()).isExactlyInstanceOf(Group.class);
        assertions.assertThat(secret.get().getName()).isEqualTo("myGroup");
        assertions.assertThat(secret.get().getParent()).isEqualTo(root);
        assertions.assertThat(secret.get().getParent().getChildren()).contains(secret.get());
        assertions.assertAll();
    }

    @Test
    @DisplayName("a user should be able to add a value to a group")
    public void aUserShouldBeAbleToAddAValueToAGroup(TestInfo testInfo) {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        ISecretService secretService = new SecretService(this.secretRepository);
        long idValue = secretService.createValue("myValue", "password", root);
        long idGroup = secretService.createGroup("myGroup", root);

        Optional<ISecret> optGroup = secretRepository.find(idGroup);
        Assumptions.assumeTrue(optGroup.isPresent());
        Assumptions.assumeTrue(optGroup.get() instanceof Group);

        Group group = (Group) optGroup.get();

        Optional<ISecret> optValue = secretRepository.find(idValue);
        Assumptions.assumeTrue(optValue.isPresent());
        Assumptions.assumeTrue(optValue.get() instanceof Value);

        Value value = (Value) optValue.get();

        secretService.addChildren(group, value);

        optGroup = secretRepository.find(idGroup);
        Assumptions.assumeTrue(optGroup.isPresent());

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(optGroup.get().getParent().getChildren()).contains(value);
        assertions.assertAll();
    }


    @Test
    @DisplayName("a user should be able to add a group to another  group")
    public void aUserShouldBeAbleToAddAGroupToAGroup(TestInfo testInfo) {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        ISecretService secretService = new SecretService(this.secretRepository);
        long id1 = secretService.createGroup("group1", root);

        Optional<ISecret> optGroup = secretRepository.find(id1);
        Assumptions.assumeTrue(optGroup.isPresent());
        Assumptions.assumeTrue(optGroup.get() instanceof Group);

        Group group = (Group) optGroup.get();

        long id2 = secretService.createGroup("group2", group);

        optGroup = secretRepository.find(id2);
        Assumptions.assumeTrue(optGroup.isPresent());

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(optGroup.get().getParent()).isEqualTo(group);
        assertions.assertAll();
    }
}
