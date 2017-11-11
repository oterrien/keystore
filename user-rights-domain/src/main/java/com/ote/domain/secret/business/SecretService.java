package com.ote.domain.secret.business;

import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.spi.IGroup;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;
import com.ote.domain.secret.spi.IValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecretService implements ISecretService {

    // region Factory
    @Getter
    private static final Factory Factory = new Factory();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class Factory {
        public SecretService create(ISecretRepository secretRepository) {
            return new SecretService(secretRepository);
        }
    }
    //endregion

    private final ISecretRepository secretRepository;

    @Override
    public long create(IValue value) {
        if (log.isTraceEnabled()) {
            log.trace("Create the value '" + value.getName());
        }
        return secretRepository.save(value);
    }

    @Override
    public long create(IGroup group) {
        if (log.isTraceEnabled()) {
            log.trace("Create the group '" + group.getName());
        }
        return secretRepository.save(group);
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
}
