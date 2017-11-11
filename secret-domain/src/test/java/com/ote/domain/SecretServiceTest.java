package com.ote.domain;

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

    private static final SecretFactory secretFactory = new SecretFactory();

    @BeforeEach
    public void setUp() {
        this.root = secretFactory.createGroup("root", null);
        ISecretRepository secretRepository = new SecretRepositoryMock();
        secretRepository.save(root);
        secretService = ServiceProvider.getInstance().getSecretService(secretRepository);
    }

    @Test
    @DisplayName("a user should be able to create a secret value")
    public void aUserShouldBeAbleToCreateASecretValue(TestInfo testInfo) throws Exception {

        log.info("################ " + testInfo.getDisplayName() + " ################");

        long id = secretService.create(secretFactory.createValue("myValue", "password", root));
        ISecret secret = secretService.find(id);

        log.debug("Secret : " + secret);

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

        long id = secretService.create(secretFactory.createGroup("myGroup", root));
        ISecret secret = secretService.find(id);

        log.debug("Secret : " + secret);

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

        long idValue = secretService.create(secretFactory.createValue("myValue", "password", root));
        long idGroup = secretService.create(secretFactory.createGroup("myGroup", root));

        Group group = secretService.find(idGroup, Group.class);
        Value value = secretService.find(idValue, Value.class);

        secretService.move(value, group);

        group = secretService.find(idGroup, Group.class);
        value = secretService.find(idValue, Value.class);

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

        long id1 = secretService.create(secretFactory.createGroup("group1", root));
        long id2 = secretService.create(secretFactory.createGroup("group2", root));

        Group group1 = secretService.find(id1, Group.class);
        Group group2 = secretService.find(id2, Group.class);

        secretService.move(group2, group1);

        group1 = secretService.find(id1, Group.class);
        group2 = secretService.find(id2, Group.class);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(root.getChildren()).doesNotContain(group2);
        assertions.assertThat(group1.getChildren()).containsExactly(group2);
        assertions.assertThat(group2.getParent()).isEqualTo(group1);
        assertions.assertAll();
    }
}
