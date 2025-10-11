package com.example.security.server.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.security.server.persistence.entity.WebAuthnPublicKeyCredentialUserEntity;

public interface WebAuthnPublicKeyCredentialUserEntityRepository extends CrudRepository<WebAuthnPublicKeyCredentialUserEntity, String> {
    WebAuthnPublicKeyCredentialUserEntity findByName(String name);
}

