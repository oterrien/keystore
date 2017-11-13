package com.ote.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Group extends AdtSecret implements IGroup {

    @JsonIgnore
    private final List<ISecret> children = new ArrayList<>();
}
