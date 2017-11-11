package com.ote.domain.model;

import com.ote.JsonUtils;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AdtSecret implements ISecret {

    private long id;
    private String name;
    private IGroup parent;

    public final void setParent(IGroup parent) {
        Optional.ofNullable(this.parent).ifPresent(p -> p.remove(this));
        this.parent = parent;
        Optional.ofNullable(parent).ifPresent(p -> p.add(this));
    }

    @Override
    public String toString(){
        try {
            return JsonUtils.serialize(this);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
