package com.ote.secret.peristence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretJpaRepository extends JpaRepository<SecretEntity, Long> {

    @Query("SELECT secret FROM SecretEntity secret WHERE secret.parent IS NULL")
    SecretEntity findRoot();
}
