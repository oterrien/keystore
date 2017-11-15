package com.ote.domain.secret.business;

import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.spi.ISecretRepository;

public interface SecretServiceFactory {

    default ISecretService createSecretService(ISecretRepository secretRepository) {
        return new SecretService(secretRepository);
    }
}