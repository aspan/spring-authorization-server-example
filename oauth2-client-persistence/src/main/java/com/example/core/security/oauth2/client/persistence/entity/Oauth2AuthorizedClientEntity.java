package com.example.core.security.oauth2.client.persistence.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.example.core.security.oauth2.client.persistence.entity.Oauth2AuthorizedClientEntity.Oauth2AuthorizedClientEntityId;

@Entity
@IdClass(Oauth2AuthorizedClientEntityId.class)
@Table(name = "security_oauth2_authorized_client")
public class Oauth2AuthorizedClientEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "client_registration_id", length = 100, nullable = false)
    @Id
    private String clientRegistrationId;
    @Column(name = "principal_name", length = 200, nullable = false)
    @Id
    private String principalName;
    @Column(name = "access_token_type", length = 100, nullable = false)
    private String accessTokenType;
    @Column(name = "access_token_value", length = 4000, nullable = false)
    private String accessTokenValue;
    @Column(name = "access_token_issued_at", nullable = false)
    private Instant accessTokenIssuedAt;
    @Column(name = "access_token_expires_at", nullable = false)
    private Instant accessTokenExpiresAt;
    @Column(name = "access_token_scopes", length = 1000)
    private String accessTokenScopes;
    @Column(name = "refresh_token_value", length = 4000)
    String refreshTokenValue;
    @Column(name = "refresh_token_issued_at")
    private Instant refreshTokenIssuedAt;
    @Column(name = "created_at timestamp", nullable = false)
    private Instant createdAtTimestamp;

    public String getClientRegistrationId() {
        return clientRegistrationId;
    }

    public void setClientRegistrationId(String clientRegistrationId) {
        this.clientRegistrationId = clientRegistrationId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getAccessTokenType() {
        return accessTokenType;
    }

    public void setAccessTokenType(String accessTokenType) {
        this.accessTokenType = accessTokenType;
    }

    public String getAccessTokenValue() {
        return accessTokenValue;
    }

    public void setAccessTokenValue(String accessTokenValue) {
        this.accessTokenValue = accessTokenValue;
    }

    public Instant getAccessTokenIssuedAt() {
        return accessTokenIssuedAt;
    }

    public void setAccessTokenIssuedAt(Instant accessTokenIssuedAt) {
        this.accessTokenIssuedAt = accessTokenIssuedAt;
    }

    public Instant getAccessTokenExpiresAt() {
        return accessTokenExpiresAt;
    }

    public void setAccessTokenExpiresAt(Instant accessTokenExpiresAt) {
        this.accessTokenExpiresAt = accessTokenExpiresAt;
    }

    public String getAccessTokenScopes() {
        return accessTokenScopes;
    }

    public void setAccessTokenScopes(String accessTokenScopes) {
        this.accessTokenScopes = accessTokenScopes;
    }

    public String getRefreshTokenValue() {
        return refreshTokenValue;
    }

    public void setRefreshTokenValue(String refreshTokenValue) {
        this.refreshTokenValue = refreshTokenValue;
    }

    public Instant getRefreshTokenIssuedAt() {
        return refreshTokenIssuedAt;
    }

    public void setRefreshTokenIssuedAt(Instant refreshTokenIssuedAt) {
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
    }

    public Instant getCreatedAtTimestamp() {
        return createdAtTimestamp;
    }

    public void setCreatedAtTimestamp(Instant createdAtTimestamp) {
        this.createdAtTimestamp = createdAtTimestamp;
    }

    public record Oauth2AuthorizedClientEntityId(String clientRegistrationId, String principalName) implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
    }
}
