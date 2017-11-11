package com.ote.secret.service;

import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.api.ServiceProvider;
import com.ote.domain.secret.business.NotFoundException;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;
import com.ote.domain.secret.spi.IValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecretServiceAdapter implements ISecretService {

    private ISecretService secretService;

    public SecretServiceAdapter(@Autowired ISecretRepository secretRepositoryAdapter){
        this.secretService = ServiceProvider.getInstance().getSecretService(secretRepositoryAdapter);
    }

    @Override
    public long create(IValue value) {
        return 0;
    }

    @Override
    public long create(IGroup group) {
        return 0;
    }

    @Override
    public void move(ISecret secret, IGroup destGroup) {

    }

    @Override
    public ISecret find(long id) throws NotFoundException {
        return null;
    }
}
