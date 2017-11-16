package com.ote.junit;

import com.ote.domain.mock.SecretRepositoryMock;
import com.ote.domain.model.Group;
import com.ote.domain.model.SecretFactory;
import com.ote.domain.model.Value;
import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.api.ServiceProvider;
import com.ote.domain.secret.business.NotFoundException;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
@Slf4j
public class SecretServiceTest {

    private ISecretService secretService;

    private IGroup root;

    @BeforeEach
    public void setUp() {
        this.root = SecretFactory.getInstance().createGroup("root", null);
        ISecretRepository secretRepository = new SecretRepositoryMock();
        secretRepository.save(root);
        secretService = ServiceProvider.getInstance().createSecretService(secretRepository);
    }

    @Test
    @DisplayName("a user should be able to create a secret value")
    public void aUserShouldBeAbleToCreateASecretValue(TestInfo testInfo) throws Exception {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        long id = secretService.create(SecretFactory.getInstance().createValue("myValue", "password", root));
        ISecret secret = secretService.find(id);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(secret).isExactlyInstanceOf(Value.class);
        assertions.assertThat(secret.getName()).isEqualTo("myValue");
        assertions.assertThat(secret.getParent()).isEqualTo(root);
        assertions.assertThat(((Value) secret).getValue()).isEqualTo("password");
        assertions.assertThat(secret.getParent().getChildren()).contains(secret);
        assertions.assertAll();
    }

    @Test
    @DisplayName("a user should not be able to find secret when not exist")
    public void aUserShouldNotBeAbleToFindSecretWhenNotExist(TestInfo testInfo) throws Exception {

        log.info("################ " + testInfo.getDisplayName() + " ################");
        assertThrows(NotFoundException.class, () -> secretService.find(1000));
    }


    @Test
    @DisplayName("a user should be able to create an empty group")
    public void aUserShouldBeAbleToCreateAnEmptyGroup(TestInfo testInfo) throws Exception {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        long id = secretService.create(SecretFactory.getInstance().createGroup("myGroup", root));
        ISecret secret = secretService.find(id);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(secret).isExactlyInstanceOf(Group.class);
        assertions.assertThat(secret.getName()).isEqualTo("myGroup");
        assertions.assertThat(secret.getParent()).isEqualTo(root);
        assertions.assertThat(secret.getParent().getChildren()).contains(secret);
        assertions.assertAll();
    }

    @Test
    @DisplayName("a user should be able to add a value to a group")
    public void aUserShouldBeAbleToAddAValueToAGroup(TestInfo testInfo) throws Exception {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        long idGroup = secretService.create(SecretFactory.getInstance().createGroup("myGroup", root));
        Group group = secretService.find(idGroup, Group.class);

        long idValue = secretService.create(SecretFactory.getInstance().createValue("myValue", "password", group));
        Value value = secretService.find(idValue, Value.class);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(root.getChildren()).doesNotContain(value);
        assertions.assertThat(group.getChildren()).containsExactly(value);
        assertions.assertThat(value.getParent()).isEqualTo(group);
        assertions.assertAll();
    }

    @Test
    @DisplayName("a user should be able to add a value to a group")
    public void aUserShouldBeAbleToMoveAValueToAGroup(TestInfo testInfo) throws Exception {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        long idValue = secretService.create(SecretFactory.getInstance().createValue("myValue", "password", root));
        long idGroup = secretService.create(SecretFactory.getInstance().createGroup("myGroup", root));

        Group group = secretService.find(idGroup, Group.class);
        Value value = secretService.find(idValue, Value.class);

        secretService.move(value, group);

        group = secretService.find(idGroup, Group.class);
        value = secretService.find(idValue, Value.class);

        log.info("Value: " + value);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(root.getChildren()).doesNotContain(value);
        assertions.assertThat(group.getChildren()).containsExactly(value);
        assertions.assertThat(value.getParent()).isEqualTo(group);
        assertions.assertAll();
    }

    @Test
    @DisplayName("a user should be able to move a group to another group")
    public void aUserShouldBeAbleToAddAGroupToAGroup(TestInfo testInfo) throws Exception {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        long id1 = secretService.create(SecretFactory.getInstance().createGroup("group1", root));
        long id2 = secretService.create(SecretFactory.getInstance().createGroup("group2", root));

        Group group1 = secretService.find(id1, Group.class);
        Group group2 = secretService.find(id2, Group.class);

        secretService.move(group2, group1);

        group1 = secretService.find(id1, Group.class);
        group2 = secretService.find(id2, Group.class);

        log.info("Group: " + group2);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(root.getChildren()).doesNotContain(group2);
        assertions.assertThat(group1.getChildren()).containsExactly(group2);
        assertions.assertThat(group2.getParent()).isEqualTo(group1);
        assertions.assertAll();
    }

    @Test
    @DisplayName("a user should be able to remove a secret")
    public void aUserShouldBeAbleToRemoveASecret(TestInfo testInfo) throws Exception {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        long idGroup1 = secretService.create(SecretFactory.getInstance().createGroup("group1", root));
        Group group1 = secretService.find(idGroup1, Group.class);

        long idGroup2 = secretService.create(SecretFactory.getInstance().createGroup("group2", group1));

        secretService.remove(idGroup2);

        group1 = secretService.find(idGroup1, Group.class);

        Assertions.assertThrows(NotFoundException.class, () -> secretService.find(idGroup2));
        org.assertj.core.api.Assertions.assertThat(group1.getChildren().stream().anyMatch(p -> p.getId() == idGroup2)).isFalse();
    }
}
