package com.ote.secret.rest;

import com.ote.secret.peristence.SecretEntity;
import com.ote.secret.peristence.SecretJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecretMapperService {

    @Value("${application.with-details.default}")
    private boolean withDetailsDefault;

    @Autowired
    private SecretJpaRepository secretRepository;

    public List<SecretPayload> convert(List<SecretEntity> entities) {
        return convert(entities, withDetailsDefault);
    }

    public List<SecretPayload> convert(List<SecretEntity> entities, boolean withDetails) {
        return entities.stream().
                map(p -> convert(p, withDetails)).
                collect(Collectors.toList());
    }

    public SecretPayload convert(SecretEntity entity) {
        return convert(entity, withDetailsDefault);
    }

    public SecretPayload convert(SecretEntity entity, boolean withDetails) {
        SecretPayload payload = new SecretPayload();
        payload.setId(entity.getId());
        payload.setName(entity.getName());
        payload.setValue(entity.getValue());
        if (entity.getParent() != null) {
            setParent(payload, entity.getParent(), withDetails);
        }
        return payload;
    }

    private void setParent(SecretPayload payload, SecretEntity parent, boolean withDetails) {
        payload.setParentId(parent.getId());
        if (withDetails) {
            SecretPayload parentPayload = new SecretPayload();
            parentPayload.setId(parent.getId());
            parentPayload.setName(parent.getName());
            parentPayload.setValue(parent.getValue());
            payload.setParent(parentPayload);
            if (parent.getParent() != null) {
                setParent(parentPayload, parent.getParent(), withDetails);
            }
        }
    }

    public SecretEntity convert(SecretPayload payload) {
        SecretEntity entity = new SecretEntity();
        entity.setId(payload.getId());
        entity.setName(payload.getName());
        entity.setValue(payload.getValue());
        if (payload.getParentId() > 0) {
            entity.setParent(secretRepository.findOne(payload.getParentId()));
        } else if (payload.getParent() != null) {
            setParent(entity, payload.getParent());
        }
        return entity;
    }

    private void setParent(SecretEntity entity, SecretPayload parent) {
        SecretEntity parentEntity = new SecretEntity();
        parentEntity.setId(parent.getId());
        parentEntity.setName(parent.getName());
        parentEntity.setValue(parent.getValue());
        entity.setParent(parentEntity);
        if (parent.getParent() != null) {
            setParent(parentEntity, parent.getParent());
        }
    }

}
