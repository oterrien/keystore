package com.ote.domain.secret.spi;

import java.util.Optional;

public interface ISecretRepository {

    long save(ISecret secret);

    Optional<ISecret> find(long id);
}
