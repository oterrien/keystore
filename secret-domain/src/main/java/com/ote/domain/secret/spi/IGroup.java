package com.ote.domain.secret.spi;

import java.util.List;

public interface IGroup extends ISecret {

    List<ISecret> getChildren();

    default void addChild(ISecret secret) {
        if (!hasChild(secret)) {
            getChildren().add(secret);
            if (secret.getParent() != this) {
                secret.setParent(this);
            }
        }
    }

    default void removeChild(ISecret secret) {
        if (hasChild(secret)) {
            getChildren().remove(secret);
            if (secret.getParent() != null) {
                secret.setParent(null);
            }
        }
    }

    default boolean hasChild(ISecret secret) {
        return getChildren().stream().anyMatch(p -> p.equals(secret));
    }
}
