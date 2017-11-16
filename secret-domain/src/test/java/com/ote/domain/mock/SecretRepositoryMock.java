package com.ote.domain.mock;

import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class SecretRepositoryMock implements ISecretRepository {

    private final List<ISecret> secretList = new ArrayList<>();

    private AtomicLong id = new AtomicLong(0);

    @Override
    public long save(ISecret secret) {

        if (secret.getId() != 0) {
            secretList.removeIf(p -> p.getId() == secret.getId());
        }

        long id = secret.getId() == 0 ? this.id.incrementAndGet() : secret.getId();
        secret.setId(id);
        secretList.add(secret);
        if (secret.getParent() != null) {
            save(secret.getParent());
        }
        return id;
    }

    public Optional<ISecret> find(long id) {
        return secretList.stream().filter(p -> p.getId() == id).findAny();
    }

    @Override
    public void delete(long id) {

        find(id).ifPresent(s ->
                Optional.ofNullable(s.getParent()).
                        ifPresent(p -> {
                            p.getChildren().removeIf(s1 -> s1.getId() == id);
                            save(p);
                        }));
        secretList.removeIf(p -> p.getId() == id);
    }

}
