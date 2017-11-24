package com.ote.secret.rest.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SecretPayload {

    private String name;

    private String description;

    private String value;
}
