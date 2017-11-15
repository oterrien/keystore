package com.ote.secret.peristence;

import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecretRepositoryAdapter implements ISecretRepository {

    @Autowired
    private SecretJpaRepository secretJpaRepository;

    @Override
    public long save(ISecret secret) {
        return secretJpaRepository.save((SecretEntity) secret).getId();
    }

    @Override
    public Optional<ISecret> find(long id) {
        return Optional.ofNullable(secretJpaRepository.findOne(id));
    }

    @Override
    public void delete(long id) {
        secretJpaRepository.delete(id);
    }
}
