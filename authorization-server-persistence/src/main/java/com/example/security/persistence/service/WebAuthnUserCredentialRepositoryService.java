package com.example.security.persistence.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.web.webauthn.api.AuthenticatorTransport;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.CredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutableCredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCose;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialType;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.example.security.persistence.entity.WebAuthnUserCredentialEntity;
import com.example.security.persistence.repository.WebAuthnUserCredentialEntityRepository;

@Service
public class WebAuthnUserCredentialRepositoryService implements UserCredentialRepository {
    private final WebAuthnUserCredentialEntityRepository webAuthnUserCredentialEntityRepository;

    public WebAuthnUserCredentialRepositoryService(WebAuthnUserCredentialEntityRepository webAuthnUserCredentialEntityRepository) {
        this.webAuthnUserCredentialEntityRepository = webAuthnUserCredentialEntityRepository;
    }

    @Override
    public void delete(@NonNull Bytes credentialId) {
        this.webAuthnUserCredentialEntityRepository.deleteById(credentialId.toBase64UrlString());
    }

    @Override
    public void save(@NonNull CredentialRecord credentialRecord) {
        this.webAuthnUserCredentialEntityRepository.save(toEntity(credentialRecord));
    }

    @Override
    public @Nullable CredentialRecord findByCredentialId(@NonNull Bytes credentialId) {
        return fromEntity(this.webAuthnUserCredentialEntityRepository.findById(credentialId.toBase64UrlString()).orElse(null));
    }

    @Override
    public @NonNull List<CredentialRecord> findByUserId(@NonNull Bytes userId) {
        return this.webAuthnUserCredentialEntityRepository.findByUserEntityUserId(userId.toBase64UrlString()).stream().map(WebAuthnUserCredentialRepositoryService::fromEntity).toList();
    }

    private static CredentialRecord fromEntity(WebAuthnUserCredentialEntity webAuthnUserCredentialEntity) {
        if (webAuthnUserCredentialEntity == null) {
            return null;
        }
        var credentialId = Bytes.fromBase64(new String(webAuthnUserCredentialEntity.getCredentialId().getBytes()));
        var userEntityUserId = Bytes.fromBase64(new String(webAuthnUserCredentialEntity.getUserEntityUserId().getBytes()));
        var publicKey = new ImmutablePublicKeyCose(webAuthnUserCredentialEntity.getPublicKey());
        var signatureCount = webAuthnUserCredentialEntity.getSignatureCount();
        var uvInitialized = webAuthnUserCredentialEntity.getUvInitialized();
        var backupEligible = webAuthnUserCredentialEntity.getBackupEligible();
        var credentialType = PublicKeyCredentialType.valueOf(webAuthnUserCredentialEntity.getPublicKeyCredentialType());
        var backupState = webAuthnUserCredentialEntity.getBackupState();

        Bytes attestationObject = null;
        var rawAttestationObject = webAuthnUserCredentialEntity.getAttestationObject();
        if (rawAttestationObject != null) {
            attestationObject = new Bytes(rawAttestationObject);
        }

        Bytes attestationClientDataJson = null;
        var rawAttestationClientDataJson = webAuthnUserCredentialEntity.getAttestationClientDataJson();
        if (rawAttestationClientDataJson != null) {
            attestationClientDataJson = new Bytes(rawAttestationClientDataJson);
        }

        var created = webAuthnUserCredentialEntity.getCreated();
        var lastUsed = webAuthnUserCredentialEntity.getLastUsed();
        var label = webAuthnUserCredentialEntity.getLabel();
        var transports = webAuthnUserCredentialEntity.getAuthenticatorTransports().split(",");

        var authenticatorTransports = new HashSet<AuthenticatorTransport>();
        for (var transport : transports) {
            authenticatorTransports.add(AuthenticatorTransport.valueOf(transport));
        }
        Assert.notNull(lastUsed, "last_used cannot be null");
        Assert.notNull(created, "created cannot be null");
        return ImmutableCredentialRecord.builder()
                                        .credentialId(credentialId)
                                        .userEntityUserId(userEntityUserId)
                                        .publicKey(publicKey)
                                        .signatureCount(signatureCount)
                                        .uvInitialized(uvInitialized)
                                        .backupEligible(backupEligible)
                                        .credentialType(credentialType)
                                        .backupState(backupState)
                                        .attestationObject(attestationObject)
                                        .attestationClientDataJSON(attestationClientDataJson)
                                        .created(created)
                                        .label(label)
                                        .lastUsed(lastUsed)
                                        .transports(authenticatorTransports)
                                        .build();
    }

    private static WebAuthnUserCredentialEntity toEntity(CredentialRecord record) {
        if (record == null) {
            return null;
        }
        var entity = new WebAuthnUserCredentialEntity();
        var transports = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(record.getTransports())) {
            for (AuthenticatorTransport transport : record.getTransports()) {
                transports.add(transport.getValue());
            }
        }

        entity.setCredentialId(record.getCredentialId().toBase64UrlString());
        entity.setUserEntityUserId(record.getUserEntityUserId().toBase64UrlString());
        entity.setPublicKey(record.getPublicKey().getBytes());
        entity.setSignatureCount(record.getSignatureCount());
        entity.setUvInitialized(record.isUvInitialized());
        entity.setBackupEligible(record.isBackupEligible());
        entity.setAuthenticatorTransports(!CollectionUtils.isEmpty(record.getTransports()) ? String.join(",", transports) : "");
        entity.setPublicKeyCredentialType(record.getCredentialType() != null ? record.getCredentialType().getValue() : null);
        entity.setBackupState(record.isBackupState());
        entity.setAttestationObject(record.getAttestationObject() != null ? record.getAttestationObject().getBytes() : null);
        entity.setAttestationClientDataJson(record.getAttestationClientDataJSON() != null ? record.getAttestationClientDataJSON().getBytes() : null);
        entity.setCreated(record.getCreated());
        entity.setLastUsed(record.getLastUsed());
        entity.setLabel(record.getLabel());
        return entity;
    }
}
