package com.ote.domain.secret.business;

import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class SecretService implements ISecretService {

    private final ISecretRepository secretRepository;

    @Override
    public long create(ISecret secret) {
        if (log.isTraceEnabled()) {
            log.trace("Create the secret '" + secret.getName());
        }
        return secretRepository.save(secret);
    }

    @Override
    public void move(ISecret secret, IGroup destGroup) {
        if (log.isTraceEnabled()) {
            log.trace("move '" + secret.getName() + "' to group '" + destGroup.getName() + "'");
        }
        secret.setParent(destGroup);
        secretRepository.save(secret);
    }

    @Override
    public ISecret find(long id) throws NotFoundException {
        return secretRepository.
                find(id).
                orElseThrow(() -> new NotFoundException("Unable to find any secret with id " + id));
    }

    @Override
    public void remove(long id) {
        if (log.isTraceEnabled()) {
            log.trace("remove secret with id: " + id);
        }
        secretRepository.delete(id);
    }
}
