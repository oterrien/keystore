package com.ote.secret.peristence;

import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.IValue;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "T_SECRET")
@EqualsAndHashCode(of = "id")
@ToString(exclude = "children")
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

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private List<SecretEntity> children;

    public void setParent(IGroup parent) {
        Optional.ofNullable(this.parent).ifPresent(par -> par.children.removeIf(child -> child.getId() == this.id));
        setParent((SecretEntity) parent);
    }

    private void setParent(SecretEntity parent) {
        this.parent = parent;
    }

}
