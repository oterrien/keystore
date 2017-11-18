package com.ote.secret.rest.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.*;

import java.util.stream.Stream;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonTypeIdResolver(SecretTypeIdResolver.class)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SecretPayload {

    private SecretType type;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty(value = "parent-id")
    private long parentId;
}
