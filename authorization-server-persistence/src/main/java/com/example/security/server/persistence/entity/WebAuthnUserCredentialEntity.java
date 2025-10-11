package com.example.security.server.persistence.entity;

import java.time.Instant;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "security_web_authn_user_credentials")
public class WebAuthnUserCredentialEntity {

    @Id
    @Column(name = "credential_id", length = 1000, nullable = false)
    private String credentialId;
    @Column(name = "user_entity_user_id", length = 1000, nullable = false)
    private String userEntityUserId;
    @Column(name = "public_key")
    @Basic
    private byte[] publicKey;
    @Column(name = "signature_count")
    private Long signatureCount;
    @Column(name = "uv_initialized")
    private Boolean uvInitialized;
    @Column(name = "backup_eligible", nullable = false)
    private Boolean backupEligible;
    @Column(name = "authenticator_transports", length = 1000, nullable = false)
    private String authenticatorTransports;
    @Column(name = "public_key_credential_type", length = 100)
    private String publicKeyCredentialType;
    @Column(name = "backup_state", nullable = false)
    private Boolean backupState;
    @Column(name = "attestation_object")
    @Basic
    private byte[] attestationObject;
    @Column(name = "attestation_client_data_json")
    @Basic
    private byte[] attestationClientDataJson;
    @Column(name = "created")
    Instant created;
    @Column(name = "last_used")
    Instant lastUsed;
    @Column(name = "label", length = 1000, nullable = false)
    private String label;

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getUserEntityUserId() {
        return userEntityUserId;
    }

    public void setUserEntityUserId(String userEntityUserId) {
        this.userEntityUserId = userEntityUserId;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public Long getSignatureCount() {
        return signatureCount;
    }

    public void setSignatureCount(Long signatureCount) {
        this.signatureCount = signatureCount;
    }

    public Boolean getUvInitialized() {
        return uvInitialized;
    }

    public void setUvInitialized(Boolean uvInitialized) {
        this.uvInitialized = uvInitialized;
    }

    public Boolean getBackupEligible() {
        return backupEligible;
    }

    public void setBackupEligible(Boolean backupEligible) {
        this.backupEligible = backupEligible;
    }

    public String getAuthenticatorTransports() {
        return authenticatorTransports;
    }

    public void setAuthenticatorTransports(String authenticatorTransports) {
        this.authenticatorTransports = authenticatorTransports;
    }

    public String getPublicKeyCredentialType() {
        return publicKeyCredentialType;
    }

    public void setPublicKeyCredentialType(String publicKeyCredentialType) {
        this.publicKeyCredentialType = publicKeyCredentialType;
    }

    public Boolean getBackupState() {
        return backupState;
    }

    public void setBackupState(Boolean backupState) {
        this.backupState = backupState;
    }

    public byte[] getAttestationObject() {
        return attestationObject;
    }

    public void setAttestationObject(byte[] attestationObject) {
        this.attestationObject = attestationObject;
    }

    public byte[] getAttestationClientDataJson() {
        return attestationClientDataJson;
    }

    public void setAttestationClientDataJson(byte[] attestationClientDataJson) {
        this.attestationClientDataJson = attestationClientDataJson;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Instant lastUsed) {
        this.lastUsed = lastUsed;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
