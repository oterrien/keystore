package com.ote.secret.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class SecretPayload {

    private long id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    private long parentId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SecretPayload parent;
}
