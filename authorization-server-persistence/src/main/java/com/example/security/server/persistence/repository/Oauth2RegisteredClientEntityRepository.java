package com.example.security.server.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.security.server.persistence.entity.Oauth2RegisteredClientEntity;

@Repository
public interface Oauth2RegisteredClientEntityRepository extends JpaRepository<Oauth2RegisteredClientEntity, String> {
    Optional<Oauth2RegisteredClientEntity> findByClientId(String clientId);
}
