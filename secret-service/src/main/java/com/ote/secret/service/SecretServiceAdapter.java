package com.ote.secret.service;

import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.api.ServiceProvider;
import com.ote.domain.secret.business.NotFoundException;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;
import com.ote.secret.peristence.SecretEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecretServiceAdapter implements ISecretService {

    private ISecretService secretService;

    public SecretServiceAdapter(@Autowired ISecretRepository secretRepository) {
        this.secretService = ServiceProvider.getInstance().createSecretService(secretRepository);
    }

    @Override
    public long create(ISecret secret) {
        return secretService.create(secret);
    }

    @Override
    public void move(ISecret secret, IGroup destGroup) {
        secretService.move(secret, destGroup);
    }

    @Override
    public SecretEntity find(long id) throws NotFoundException {
        return (SecretEntity) secretService.find(id);
    }

    @Override
    public void remove(long id) {
        secretService.remove(id);
    }
}
