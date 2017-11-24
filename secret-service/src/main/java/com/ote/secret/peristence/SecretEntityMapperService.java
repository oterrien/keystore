package com.ote.secret.peristence;

import com.ote.domain.secret.api.model.Secret;
import com.ote.secret.rest.payload.SecretPayload;
import org.springframework.stereotype.Service;

@Service
public class SecretEntityMapperService {


    public Secret convert(SecretEntity entity) {
        Secret secret = new Secret();
        secret.setName(entity.getName());
        secret.setDescription(entity.getDescription());
        secret.setValue(entity.getValue());
        return secret;
    }

    public SecretEntity convert(Secret secret) {
        SecretEntity entity = new SecretEntity();
        entity.setName(secret.getName());
        entity.setDescription(secret.getDescription());
        entity.setValue(secret.getValue());
        return entity;
    }


}
