package com.ote.secret.service;

import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.api.ServiceProvider;
import com.ote.domain.secret.api.model.Secret;
import com.ote.domain.secret.business.NotFoundException;
import com.ote.domain.secret.spi.ISecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecretServiceAdapter implements ISecretService {

    private ISecretService secretService;

    public SecretServiceAdapter(@Autowired ISecretRepository secretRepository) {
        this.secretService = ServiceProvider.getInstance().createSecretService(secretRepository);
    }

    @Override
    public void create(Secret secret) {
        secretService.create(secret);
    }

    @Override
    public Secret find(String name) throws NotFoundException {
        return secretService.find(name);
    }
}
