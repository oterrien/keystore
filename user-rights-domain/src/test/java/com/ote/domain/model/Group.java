package com.ote.domain.model;

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

    private final List<ISecret> children = new ArrayList<>();

    public ISecret[] getChildren() {
        return this.children.toArray(new ISecret[0]);
    }

    public void add(ISecret children) {
        this.children.add(children);
    }

    public void remove(ISecret children) {
        this.children.remove(children);
    }
}
