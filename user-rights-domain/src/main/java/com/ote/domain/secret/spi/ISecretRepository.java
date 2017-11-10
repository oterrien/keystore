package com.ote.domain.secret.spi;

import java.util.Optional;

public interface ISecretRepository {

    long create(ISecret secret);

    Optional<ISecret> find(long id);

    void update(long id, ISecret secret);
}
