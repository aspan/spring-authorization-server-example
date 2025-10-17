package com.example.security.server.persistence.service;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;
import org.springframework.stereotype.Service;

import com.example.security.server.persistence.entity.WebAuthnPublicKeyCredentialUserEntity;
import com.example.security.server.persistence.repository.WebAuthnPublicKeyCredentialUserEntityRepository;

@Service
public class WebAuthnPublicKeyCredentialUserEntityRepositoryService implements PublicKeyCredentialUserEntityRepository {
    private final WebAuthnPublicKeyCredentialUserEntityRepository webAuthnPublicKeyCredentialUserEntityRepository;

    public WebAuthnPublicKeyCredentialUserEntityRepositoryService(WebAuthnPublicKeyCredentialUserEntityRepository webAuthnPublicKeyCredentialUserEntityRepository) {
        this.webAuthnPublicKeyCredentialUserEntityRepository = webAuthnPublicKeyCredentialUserEntityRepository;
    }


    @Override
    public @Nullable PublicKeyCredentialUserEntity findById(Bytes id) {
        return fromEntity(this.webAuthnPublicKeyCredentialUserEntityRepository.findById(id.toBase64UrlString()).orElse(null));
    }

    @Override
    public @Nullable PublicKeyCredentialUserEntity findByUsername(@NonNull String username) {
        return fromEntity(this.webAuthnPublicKeyCredentialUserEntityRepository.findByName(username));
    }

    @Override
    public void save(@NonNull PublicKeyCredentialUserEntity publicKeyCredentialUserEntity) {
        var entity = new WebAuthnPublicKeyCredentialUserEntity();
        var persistentEntity = this.webAuthnPublicKeyCredentialUserEntityRepository.findById(publicKeyCredentialUserEntity.getId().toBase64UrlString());
        if (persistentEntity.isPresent()) {
            entity = persistentEntity.get();
        }
        this.webAuthnPublicKeyCredentialUserEntityRepository.save(toEntity(entity, publicKeyCredentialUserEntity));
    }

    @Override
    public void delete(Bytes id) {
        this.webAuthnPublicKeyCredentialUserEntityRepository.deleteById(id.toBase64UrlString());
    }

    private PublicKeyCredentialUserEntity fromEntity(WebAuthnPublicKeyCredentialUserEntity webAuthnPublicKeyCredentialUserEntity) {
        if (webAuthnPublicKeyCredentialUserEntity == null) {
            return null;
        }
        return ImmutablePublicKeyCredentialUserEntity.builder()
                                                     .id(Bytes.fromBase64(new String(webAuthnPublicKeyCredentialUserEntity.getId().getBytes())))
                                                     .name(webAuthnPublicKeyCredentialUserEntity.getName())
                                                     .displayName(webAuthnPublicKeyCredentialUserEntity.getDisplayName())
                                                     .build();
    }

    private WebAuthnPublicKeyCredentialUserEntity toEntity(WebAuthnPublicKeyCredentialUserEntity entity, PublicKeyCredentialUserEntity publicKeyCredentialUserEntity) {
        if (publicKeyCredentialUserEntity == null) {
            return null;
        }
        entity.setId(publicKeyCredentialUserEntity.getId().toBase64UrlString());
        entity.setName(publicKeyCredentialUserEntity.getName());
        entity.setDisplayName(publicKeyCredentialUserEntity.getDisplayName());
        return entity;
    }
}
