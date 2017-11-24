package com.ote.cucumber;

import com.ote.domain.secret.api.model.Secret;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SecretBuilder {

    private final List<Field> fields;

    public Secret build() {
        Secret secret = new Secret();
        fields.forEach(p -> {
            try {
                java.lang.reflect.Field field = Secret.class.getDeclaredField(p.getField());
                field.setAccessible(true);
                field.set(secret, p.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return secret;
    }
}
