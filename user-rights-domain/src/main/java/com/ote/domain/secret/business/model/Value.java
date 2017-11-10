package com.ote.domain.secret.business.model;

import com.ote.domain.secret.spi.ISecret;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Value implements ISecret {

    private long id;
    private String name;
    private String value;
    private Group parent;

}
