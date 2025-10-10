package com.example.security.persistence.service;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;
import org.springframework.stereotype.Service;

import com.example.security.persistence.entity.WebAuthnPublicKeyCredentialUserEntity;
import com.example.security.persistence.repository.WebAuthnPublicKeyCredentialUserEntityRepository;

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
    public void save(@NonNull PublicKeyCredentialUserEntity userEntity) {
        this.webAuthnPublicKeyCredentialUserEntityRepository.save(toEntity(userEntity));
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

    private WebAuthnPublicKeyCredentialUserEntity toEntity(PublicKeyCredentialUserEntity publicKeyCredentialUserEntity) {
        if (publicKeyCredentialUserEntity == null) {
            return null;
        }
        var entity = new WebAuthnPublicKeyCredentialUserEntity();
        entity.setId(publicKeyCredentialUserEntity.getId().toBase64UrlString());
        entity.setName(publicKeyCredentialUserEntity.getName());
        entity.setDisplayName(publicKeyCredentialUserEntity.getDisplayName());
        return entity;
    }
}
