package com.ote.secret.service;

import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.IValue;
import com.ote.secret.peristence.SecretEntity;
import com.ote.secret.peristence.SecretJpaRepository;
import com.ote.secret.rest.payload.GroupPayload;
import com.ote.secret.rest.payload.SecretPayload;
import com.ote.secret.rest.payload.ValuePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecretMapperService {

    @Autowired
    private SecretJpaRepository secretJpaRepository;

    //region convert payload to entity
    public SecretEntity convert(SecretPayload payload) {
        switch (payload.getType()) {
            case GROUP:
                return convert((GroupPayload) payload);
            case VALUE:
            default:
                return convert((ValuePayload) payload);
        }
    }

    private SecretEntity convert(ValuePayload payload) {
        SecretEntity valueEntity = new SecretEntity();
        valueEntity.setId(Optional.ofNullable(payload.getId()).orElse(0L));
        valueEntity.setName(payload.getName());
        valueEntity.setParent(Optional.ofNullable(secretJpaRepository.findOne(payload.getParentId())).orElse(secretJpaRepository.findRoot()));
        valueEntity.setValue(payload.getValue());
        return valueEntity;
    }

    private SecretEntity convert(GroupPayload payload) {
        SecretEntity groupEntity = new SecretEntity();
        groupEntity.setId(payload.getId());
        groupEntity.setName(payload.getName());
        groupEntity.setParent(secretJpaRepository.findOne(payload.getParentId()));
        payload.getChildren().forEach(childId -> groupEntity.getChildren().add(secretJpaRepository.findOne(childId)));
        return groupEntity;
    }
    //endregion

    // region convert entity to payload
    public SecretPayload convert(SecretEntity secret) {
        if (secret.getValue() != null) {
            return convert((IValue) secret);
        }
        return convert((IGroup) secret);
    }

    private ValuePayload convert(IValue secret) {
        ValuePayload payload = new ValuePayload();
        payload.setId(secret.getId());
        payload.setName(secret.getName());
        payload.setParentId(Optional.ofNullable(secret.getParent()).map(ISecret::getId).orElse(null));
        payload.setValue(secret.getValue());
        return payload;
    }

    private GroupPayload convert(IGroup secret) {
        GroupPayload payload = new GroupPayload();
        payload.setId(secret.getId());
        payload.setName(secret.getName());
        payload.setParentId(Optional.ofNullable(secret.getParent()).map(ISecret::getId).orElse(null));
        secret.getChildren().forEach(p -> payload.getChildren().add(p.getId()));
        return payload;
    }
    //endregion
}
