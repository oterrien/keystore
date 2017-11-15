package com.ote.domain.secret.api;

import com.ote.domain.secret.business.SecretServiceFactory;
import com.ote.domain.secret.spi.ISecretRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServiceProvider implements SecretServiceFactory {

    @Getter
    public static final ServiceProvider Instance = new ServiceProvider();
}
