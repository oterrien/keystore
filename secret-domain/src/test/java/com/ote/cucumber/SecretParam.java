package com.ote.cucumber;

import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import lombok.Data;

@Data
public class SecretParam {

    private Type type;
    private String name;
    private String value;
    private String parent;

    public ISecret create(){
        return create(null);
    }

    public ISecret create(IGroup group){
        return type.create(this, group);
    }
}
