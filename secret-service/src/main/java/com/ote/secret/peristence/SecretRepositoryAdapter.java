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


        return 0;
    }

    @Override
    public Optional<ISecret> find(long id) {

        return null;
    }
}
