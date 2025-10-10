package com.example.security.persistence.repository;

import java.time.Instant;

import org.springframework.data.repository.CrudRepository;

import com.example.security.persistence.entity.OneTimeTokenEntity;

public interface OneTimeTokenEntityRepository extends CrudRepository<OneTimeTokenEntity, String> {
    void deleteByExpiresAtBefore(Instant expiresAt);
}
