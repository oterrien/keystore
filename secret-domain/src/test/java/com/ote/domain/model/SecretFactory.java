package com.ote.domain.model;

import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.IValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecretFactory {

    @Getter
    private static final SecretFactory Instance = new SecretFactory();

    public IGroup createGroup(String name, IGroup parent) {
        Group group = new Group();
        group.setName(name);
        group.setParent(parent);
        return group;
    }

    public IValue createValue(String name, String secretValue, IGroup parent) {
        Value value = new Value();
        value.setName(name);
        value.setValue(secretValue);
        value.setParent(parent);
        return value;
    }
}
