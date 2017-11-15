package com.ote.secret.rest.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GroupPayload extends SecretPayload {

    public GroupPayload(){
        this.setType(SecretType.GROUP);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Long> children = new ArrayList<>();
}
