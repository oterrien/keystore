package com.ote.domain.secret.spi;

import com.ote.domain.secret.business.model.Group;

public interface ISecret {

    long getId();

    void setId(long id);

    String getName();

    void setName(String name);

    Group getParent();

    void setParent(Group parent);
}
