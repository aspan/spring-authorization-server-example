package com.example.security.server.persistence.repository;

import java.time.Instant;

import org.springframework.data.repository.CrudRepository;

import com.example.security.server.persistence.entity.OneTimeTokenEntity;

public interface OneTimeTokenEntityRepository extends CrudRepository<OneTimeTokenEntity, String> {
    void deleteByExpiresAtBefore(Instant expiresAt);
}
