package com.example.security.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "security_web_authn_user_entities")
public class WebAuthnPublicKeyCredentialUserEntity {
    @Id
    @Column(name = "id", length = 1000, nullable = false)
    private String id;
    @Column(name = "name", length = 1000, nullable = false)
    private String name;
    @Column(name = "display_name", length = 200)
    private String displayName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
