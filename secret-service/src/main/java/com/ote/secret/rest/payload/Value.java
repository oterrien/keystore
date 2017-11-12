package com.ote.secret.rest.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Value extends SecretPayload {

    private String value;
}
