package com.ote.secret.peristence;

import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.IValue;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "T_SECRET")
@EqualsAndHashCode(of = "id")
public class SecretEntity implements IGroup, IValue, Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    protected long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "VALUE")
    private String value;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    protected SecretEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private final List<SecretEntity> children = new ArrayList<>();

    public void setParent(IGroup parent) {
        Optional.ofNullable(this.parent).ifPresent(p -> {
            if (p.hasChild(this)) {
                p.removeChild(this);
            }
        });
        setParent((SecretEntity) parent);
        Optional.ofNullable(this.parent).ifPresent(p -> {
            if (!p.hasChild(this)) {
                p.addChild(this);
            }
        });
    }

    private void setParent(SecretEntity parent) {
        this.parent = parent;
    }

    public List<ISecret> getChildren() {
        return children.stream().map(p -> (ISecret) p).collect(Collectors.toList());
    }

}
