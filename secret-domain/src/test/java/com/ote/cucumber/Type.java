package com.ote.cucumber;

import com.ote.domain.model.SecretFactory;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;

public enum Type {
    Value((secret, parent) -> SecretFactory.getInstance().createValue(secret.getName(), secret.getValue(), parent)),
    Group((secret, parent) -> SecretFactory.getInstance().createGroup(secret.getName(), parent));

    private final SecretCreator creator;

    Type(SecretCreator creator) {
        this.creator = creator;
    }

    public ISecret create(SecretParam secretParam, IGroup parent){
        return creator.create(secretParam, parent);
    }

    @FunctionalInterface
    interface SecretCreator {
        ISecret create(SecretParam secretParam, IGroup parent);
    }
}
