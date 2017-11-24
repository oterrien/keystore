package com.ote.secret.peristence;

import com.ote.domain.secret.api.model.Secret;
import com.ote.domain.secret.spi.ISecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecretRepositoryAdapter implements ISecretRepository {

    @Autowired
    private SecretEntityMapperService secretEntityMapperService;

    @Autowired
    private SecretJpaRepository secretJpaRepository;

    @Override
    public void save(Secret secret) {
        secretJpaRepository.save(secretEntityMapperService.convert(secret));
    }

    @Override
    public Optional<Secret> find(String name) {

        return Optional.ofNullable(secretJpaRepository.findOne(name)).
                map(p -> secretEntityMapperService.convert(p));
    }
}
