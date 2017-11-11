package com.ote.domain.secret.spi;

public interface IValue extends ISecret {

    String getValue();

    void setValue(String value);
}
