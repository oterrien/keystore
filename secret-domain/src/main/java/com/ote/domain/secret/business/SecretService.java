package com.ote.domain.secret.business;

import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.api.model.Secret;
import com.ote.domain.secret.spi.ISecretRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class SecretService implements ISecretService {

    private final ISecretRepository secretRepository;

    @Override
    public void create(Secret secret) {
        if (log.isTraceEnabled()) {
            log.trace("Creating the secret '" + secret.getName());
        }
        secretRepository.save(secret);
    }

    @Override
    public Secret find(String name) throws NotFoundException {
        if (log.isTraceEnabled()) {
            log.trace("Finding the secret '" + name);
        }
        return secretRepository.find(name).orElseThrow(() -> new NotFoundException(name));
    }
}
