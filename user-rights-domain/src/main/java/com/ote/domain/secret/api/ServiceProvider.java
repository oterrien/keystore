package com.ote.domain.secret.api;

import com.ote.domain.secret.business.SecretService;
import com.ote.domain.secret.spi.ISecretRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServiceProvider {

    @Getter
    public static final ServiceProvider Instance = new ServiceProvider();

    private ISecretService secretService;

    public ISecretService getSecretService(ISecretRepository secretRepository) {
        if (secretService == null) {
            synchronized (this) {
                if (secretService == null) {
                    secretService = SecretService.getFactory().create(secretRepository);
                }
            }
        }
        return secretService;
    }
}
