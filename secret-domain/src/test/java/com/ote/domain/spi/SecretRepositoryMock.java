package com.ote.domain.spi;

import com.ote.domain.secret.api.model.Secret;
import com.ote.domain.secret.spi.ISecretRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SecretRepositoryMock implements ISecretRepository {

    private final List<Secret> secretList = new ArrayList<>();

    @Override
    public void save(Secret secret) {

        secretList.removeIf(p -> p.getName().equals(secret.getName()));
        secretList.add(secret);
    }

    public Optional<Secret> find(String name) {
        return secretList.stream().filter(p -> p.getName().equals(name)).findAny();
    }


}
