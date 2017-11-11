package com.ote.domain.model;

import com.ote.domain.secret.spi.IValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Value extends AdtSecret implements IValue {

    private String value;
}
