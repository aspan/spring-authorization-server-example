package com.example.security.server.persistence.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "security_oauth2_client_registration")
public class Oauth2ClientRegistrationEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "client_registration_id", length = 100)
    private String clientRegistrationId;
    @Column(name = "client_id", length = 100)
    private String clientId;
    @Column(name = "client_secret", length = 100)
    private String clientSecret;
    @Column(name = "client_authentication_method", length = 100)
    private String clientAuthenticationMethod;
    @Column(name = "client_autorization_grant_type", length = 100)
    private String clientAuthorizationGrantType;
    @Column(name = "client_redirect_uri")
    private String clientRedirectUri;
    @Column(name = "client_scopes", columnDefinition = "TEXT")
    private String clientScopes;
    @Column(name = "client_name", length = 100)
    private String clientName;
    @Column(name = "provider_authorization_uri")
    private String providerAuthorizationUri;
    @Column(name = "provider_token_uri")
    private String providerTokenUri;
    @Column(name = "provider_user_info_endpoint_uri")
    private String providerUserInfoEndpointUri;
    @Column(name = "provider_user_info_endpoint_authentication_method", length = 100)
    private String providerUserInfoEndpointAuthenticationMethod;
    @Column(name = "provider_user_info_endpoint_user_name_attribute_name", length = 100)
    private String providerUserInfoEndpointUserNameAttributeName;
    @Column(name = "provider_jwk_set_uri")
    private String providerJwkSetUri;
    @Column(name = "provider_issuer_uri")
    private String providerIssuerUri;
    @Column(name = "provider_configuration_metadata", columnDefinition = "TEXT")
    private String providerConfigurationMetadata;

    public String getClientRegistrationId() {
        return clientRegistrationId;
    }

    public void setClientRegistrationId(String clientRegistrationId) {
        this.clientRegistrationId = clientRegistrationId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientAuthenticationMethod() {
        return clientAuthenticationMethod;
    }

    public void setClientAuthenticationMethod(String clientAuthenticationMethod) {
        this.clientAuthenticationMethod = clientAuthenticationMethod;
    }

    public String getClientAuthorizationGrantType() {
        return clientAuthorizationGrantType;
    }

    public void setClientAuthorizationGrantType(String clientAuthorizationGrantType) {
        this.clientAuthorizationGrantType = clientAuthorizationGrantType;
    }

    public String getClientRedirectUri() {
        return clientRedirectUri;
    }

    public void setClientRedirectUri(String clientRedirectUri) {
        this.clientRedirectUri = clientRedirectUri;
    }

    public String getClientScopes() {
        return clientScopes;
    }

    public void setClientScopes(String clientScopes) {
        this.clientScopes = clientScopes;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProviderAuthorizationUri() {
        return providerAuthorizationUri;
    }

    public void setProviderAuthorizationUri(String providerAuthorizationUri) {
        this.providerAuthorizationUri = providerAuthorizationUri;
    }

    public String getProviderTokenUri() {
        return providerTokenUri;
    }

    public void setProviderTokenUri(String providerTokenUri) {
        this.providerTokenUri = providerTokenUri;
    }

    public String getProviderUserInfoEndpointUri() {
        return providerUserInfoEndpointUri;
    }

    public void setProviderUserInfoEndpointUri(String providerUserInfoEndpointUri) {
        this.providerUserInfoEndpointUri = providerUserInfoEndpointUri;
    }

    public String getProviderUserInfoEndpointAuthenticationMethod() {
        return providerUserInfoEndpointAuthenticationMethod;
    }

    public void setProviderUserInfoEndpointAuthenticationMethod(String providerUserInfoEndpointAuthenticationMethod) {
        this.providerUserInfoEndpointAuthenticationMethod = providerUserInfoEndpointAuthenticationMethod;
    }

    public String getProviderUserInfoEndpointUserNameAttributeName() {
        return providerUserInfoEndpointUserNameAttributeName;
    }

    public void setProviderUserInfoEndpointUserNameAttributeName(String providerUserInfoEndpointUserNameAttributeName) {
        this.providerUserInfoEndpointUserNameAttributeName = providerUserInfoEndpointUserNameAttributeName;
    }

    public String getProviderJwkSetUri() {
        return providerJwkSetUri;
    }

    public void setProviderJwkSetUri(String providerJwkSetUri) {
        this.providerJwkSetUri = providerJwkSetUri;
    }

    public String getProviderIssuerUri() {
        return providerIssuerUri;
    }

    public void setProviderIssuerUri(String providerIssuerUri) {
        this.providerIssuerUri = providerIssuerUri;
    }

    public String getProviderConfigurationMetadata() {
        return providerConfigurationMetadata;
    }

    public void setProviderConfigurationMetadata(String providerConfigurationMetadata) {
        this.providerConfigurationMetadata = providerConfigurationMetadata;
    }
}
