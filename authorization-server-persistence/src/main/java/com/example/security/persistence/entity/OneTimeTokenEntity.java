package com.example.security.persistence.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "security_one_time_tokens")
public class OneTimeTokenEntity {
    @Id
    @Column(name = "token_value", length = 36, nullable = false)
    private String tokenValue;
    @Column(name = "username", length = 200, nullable = false)
    private String username;
    @Column(name = "expires_at")
    private Instant expiresAt;

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenId) {
        this.tokenValue = tokenId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
