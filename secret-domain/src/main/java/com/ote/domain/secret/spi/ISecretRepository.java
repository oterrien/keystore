package com.ote.domain.secret.spi;

import com.ote.domain.secret.api.model.Secret;

import java.util.Optional;

public interface ISecretRepository {

    void save(Secret secret);

    Optional<Secret> find(String name);
}
