package com.ote.domain.model;

import com.ote.JsonUtils;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public abstract class AdtSecret implements ISecret {

    private long id;
    private String name;
    private IGroup parent;

    public final void setParent(IGroup parent) {
        Optional.ofNullable(this.parent).ifPresent(p -> {
            if (p.getChildren().stream().anyMatch(p1 -> p1.equals(this))) {
                p.getChildren().remove(this);
            }
        });
        this.parent = parent;
        Optional.ofNullable(parent).ifPresent(p -> {
            if (p.getChildren().stream().noneMatch(p1 -> p1.equals(this))) {
                p.getChildren().add(this);
            }
        });
    }

    @Override
    public String toString() {
        try {
            return JsonUtils.serialize(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
