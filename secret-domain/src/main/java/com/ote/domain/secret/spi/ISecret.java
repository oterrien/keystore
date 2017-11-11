package com.ote.domain.secret.spi;

public interface ISecret {

    long getId();

    void setId(long id);

    String getName();

    void setName(String name);

    IGroup getParent();

    void setParent(IGroup parent);
}
