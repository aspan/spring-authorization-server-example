package com.example.security.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.security.persistence.entity.WebAuthnPublicKeyCredentialUserEntity;

public interface WebAuthnPublicKeyCredentialUserEntityRepository extends CrudRepository<WebAuthnPublicKeyCredentialUserEntity, String> {
    WebAuthnPublicKeyCredentialUserEntity findByName(String name);
}

