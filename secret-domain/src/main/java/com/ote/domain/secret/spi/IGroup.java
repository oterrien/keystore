package com.ote.domain.secret.spi;

import java.util.List;

public interface IGroup extends ISecret {

    <T extends ISecret> List<T> getChildren();
}
