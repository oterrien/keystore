package com.ote.junit;

import com.ote.domain.secret.business.NotFoundException;
import com.ote.domain.spi.SecretRepositoryMock;
import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.api.ServiceProvider;
import com.ote.domain.secret.api.model.Secret;
import com.ote.domain.secret.spi.ISecretRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Slf4j
public class SecretServiceTest {

    private static final ISecretService secretService = ServiceProvider.getInstance().createSecretService(new SecretRepositoryMock());

    @Test
    @DisplayName("a user should be able to create a secret")
    public void aUserShouldBeAbleToCreateASecret() throws Throwable {

        Secret secret = new Secret();
        secret.setName("mySecret");
        secret.setDescription("my secret is very strong");
        secret.setValue("hidden");

        secretService.create(secret);

        Secret secretReloaded = secretService.find("mySecret");

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(secretReloaded.getName()).isEqualTo(secret.getName());
        assertions.assertThat(secretReloaded.getDescription()).isEqualTo(secret.getDescription());
        assertions.assertThat(secretReloaded.getValue()).isEqualTo(secret.getValue());
        assertions.assertAll();
    }


    @Test
    @DisplayName("a user should not be able to find a non existing secret")
    public void aUserShouldNotBeAbleToFindNonExistingSecret() throws Throwable {

        Assertions.assertThatThrownBy(() -> secretService.find("NotExist")).
                isInstanceOf(NotFoundException.class);
    }
}