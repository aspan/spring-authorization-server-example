package com.example.security.server.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.security.server.persistence.entity.Oauth2AuthorizationConsentEntity;

@Repository
public interface Oauth2AuthorizationConsentEntityRepository extends JpaRepository<Oauth2AuthorizationConsentEntity, Oauth2AuthorizationConsentEntity.AuthorizationConsentId> {
    Optional<Oauth2AuthorizationConsentEntity> findByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

    @Transactional
    void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}
