package com.example.security.persistence.repository;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;

import com.example.security.persistence.entity.WebAuthnUserCredentialEntity;

public interface WebAuthnUserCredentialEntityRepository extends CrudRepository<WebAuthnUserCredentialEntity, String> {
    List<WebAuthnUserCredentialEntity> findByUserEntityUserId(@NonNull String userEntityUserId);
}
