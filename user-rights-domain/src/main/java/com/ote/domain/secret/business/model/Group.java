package com.ote.domain.secret.business.model;

import com.ote.domain.secret.spi.ISecret;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@ToString(exclude = "children")
@EqualsAndHashCode(of = "id")
public class Group implements ISecret {

    private long id;
    private String name;
    private Group parent;

    private final Set<ISecret> children = new HashSet<>();

    public ISecret[] getChildren() {
        return this.children.toArray(new ISecret[0]);
    }

    public void addChildren(ISecret... children) {
        this.children.addAll(Arrays.asList(children));
    }

    public void removeChildren(ISecret... children){
        List<ISecret> childrenAsList = Arrays.asList(children);
        this.children.removeIf(p -> childrenAsList.stream().anyMatch(p1 -> p1.getId() == p.getId()));
    }
}
