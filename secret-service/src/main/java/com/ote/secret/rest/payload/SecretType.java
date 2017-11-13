package com.ote.secret.rest.payload;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public enum SecretType {
    GROUP("Group", GroupPayload.class),
    VALUE("Value", ValuePayload.class);

    private final String type;

    @JsonValue
    public String getType() {
        return this.type;
    }

    @Getter
    private final Class clazz;

    public static SecretType of(String type) {
        return Stream.of(SecretType.values()).
                filter(p -> p.type.equalsIgnoreCase(type)).
                findAny().
                orElseThrow(() -> new RuntimeException("Unable to find Type for type: " + type));
    }

    public static SecretType of(Class clazz) {
        return Stream.of(SecretType.values()).
                filter(p -> p.clazz.equals(clazz)).
                findAny().
                orElseThrow(() -> new RuntimeException("Unable to find Type for class: " + clazz.getSimpleName()));
    }
}
