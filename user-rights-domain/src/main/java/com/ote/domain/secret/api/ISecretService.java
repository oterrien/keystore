package com.ote.domain.secret.api;

import com.ote.domain.secret.business.model.Group;
import com.ote.domain.secret.spi.ISecret;

public interface ISecretService {

    long createValue(String name, String secretValue, Group parent);

    long createGroup(String name, Group parent, ISecret... children);

    void addChildren(Group group, ISecret... children);

}
