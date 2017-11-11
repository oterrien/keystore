package com.ote.domain.secret.api;

import com.ote.domain.secret.business.NotFoundException;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.IValue;

public interface ISecretService {

    long create(IValue value);

    long create(IGroup group);

    void move(ISecret secret, IGroup destGroup);

    ISecret find(long id) throws NotFoundException;

    default <T extends ISecret> T find(long id, Class<T> clazz) throws NotFoundException {
        return clazz.cast(find(id));
    }
}
