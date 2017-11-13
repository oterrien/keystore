package com.ote.secret.rest.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GroupPayload extends SecretPayload {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Long> children = new ArrayList<>();
}
