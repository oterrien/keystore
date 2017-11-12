package com.ote.secret.peristence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "T_SECRET_VALUE")
@NoArgsConstructor
public class GroupEntity extends SecretEntity {

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private final List<SecretEntity> children = new ArrayList<>();

    public void addChildren(SecretEntity entity) {
        this.children.add(entity);
        entity.setParent(this);
    }

    public void removeChildren(SecretEntity entity) {
        this.children.removeIf(p -> p.getId() == entity.getId());
    }
}
