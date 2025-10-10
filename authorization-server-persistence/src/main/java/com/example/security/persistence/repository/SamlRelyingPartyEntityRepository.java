package com.example.security.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.security.persistence.entity.SamlRelyingPartyEntity;

@Repository
public interface SamlRelyingPartyEntityRepository extends JpaRepository<SamlRelyingPartyEntity, String> {
    Optional<SamlRelyingPartyEntity> findByAssertingPartyMetadata_entityId(String entityId);
}
