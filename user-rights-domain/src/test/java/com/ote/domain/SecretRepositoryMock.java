package com.ote.domain;

import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SecretRepositoryMock implements ISecretRepository {

    private final Set<ISecret> secretList = new HashSet<>();

    public long create(ISecret secret) {
        long id = secretList.size() + 1;
        secret.setId(id);
        secretList.add(secret);
        return id;
    }

    public Optional<ISecret> find(long id) {
        return secretList.stream().filter(p -> p.getId() == id).findAny();
    }

    @Override
    public void update(long id, ISecret secret) {
        secretList.removeIf(p -> p.getId() == id);
        secret.setId(id);
        secretList.add(secret);
    }
}
