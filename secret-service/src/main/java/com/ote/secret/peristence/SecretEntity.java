package com.ote.secret.peristence;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SecretEntity{

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private GroupEntity parent;

    public void setParent(GroupEntity parent){
        this.parent = parent;
        if (!parent.getChildren().contains(this)){
            parent.addChildren(this);
        }
    }
}

/*
public class SecretEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "VALUE")
    private String value;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private SecretEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private final List<SecretEntity> children = new ArrayList<>();
}
*/
