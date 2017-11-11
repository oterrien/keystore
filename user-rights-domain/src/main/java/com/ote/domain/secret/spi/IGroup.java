package com.ote.domain.secret.spi;

public interface IGroup extends ISecret {

    ISecret[] getChildren();

    void add(ISecret children);

    void remove(ISecret children);
}
