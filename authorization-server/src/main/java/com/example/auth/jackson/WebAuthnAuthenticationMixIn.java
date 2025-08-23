package com.example.auth.jackson;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(value = "authenticated", ignoreUnknown = true)
public class WebAuthnAuthenticationMixIn {
    public WebAuthnAuthenticationMixIn(@JsonProperty("principal") PublicKeyCredentialUserEntity principal,
                                       @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
    }
}
