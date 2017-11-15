package com.ote.secret.rest.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ValuePayload extends SecretPayload {

    public ValuePayload(){
        this.setType(SecretType.GROUP);
    }

    private String value;
}
