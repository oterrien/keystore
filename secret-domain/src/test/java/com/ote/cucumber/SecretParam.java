package com.ote.cucumber;

import lombok.Data;

@Data
public class SecretParam {

    private Type type;
    private String name;
    private String value;
    private String parent;
}
