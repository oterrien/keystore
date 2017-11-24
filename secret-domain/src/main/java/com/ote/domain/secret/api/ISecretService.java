package com.ote.domain.secret.api;

import com.ote.domain.secret.business.NotFoundException;
import com.ote.domain.secret.api.model.Secret;

public interface ISecretService {

    void create(Secret secret);

    Secret find(String name) throws NotFoundException;
}
