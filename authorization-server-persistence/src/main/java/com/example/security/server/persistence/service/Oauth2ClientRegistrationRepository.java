package com.example.security.server.persistence.service;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Service;

import com.example.security.server.persistence.entity.Oauth2ClientRegistrationEntity;
import com.example.security.server.persistence.repository.Oauth2ClientRegistrationEntityRepository;

@Service
public class Oauth2ClientRegistrationRepository implements ClientRegistrationRepository {
    private final Oauth2ClientRegistrationEntityRepository oauth2ClientRegistrationEntityRepository;
    private final JsonParser jsonParser;

    public Oauth2ClientRegistrationRepository(Oauth2ClientRegistrationEntityRepository oauth2ClientRegistrationEntityRepository, JsonParser jsonParser) {
        this.oauth2ClientRegistrationEntityRepository = oauth2ClientRegistrationEntityRepository;
        this.jsonParser = jsonParser;
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return oauth2ClientRegistrationEntityRepository.findById(registrationId)
                                                       .map(this::fromEntity)
                                                       .orElse(null);
    }

    public ClientRegistration save(ClientRegistration clientRegistration) {
        return fromEntity(oauth2ClientRegistrationEntityRepository.save(toEntity(clientRegistration)));
    }

    public ClientRegistration fromEntity(Oauth2ClientRegistrationEntity entity) {
        var builder =
                ClientRegistration.withRegistrationId(entity.getClientRegistrationId())
                                  .clientId(entity.getClientId())
                                  .clientSecret(entity.getClientSecret())
                                  .clientName(entity.getClientName())
                                  .redirectUri(entity.getClientRedirectUri())
                                  .scope(convertScopes(entity.getClientScopes()))
                                  .authorizationUri(entity.getProviderAuthorizationUri())
                                  .tokenUri(entity.getProviderTokenUri())
                                  .userInfoUri(entity.getProviderUserInfoEndpointUri())
                                  .userNameAttributeName(entity.getProviderUserInfoEndpointUserNameAttributeName())
                                  .jwkSetUri(entity.getProviderJwkSetUri())
                                  .issuerUri(entity.getProviderIssuerUri())
                                  .providerConfigurationMetadata(jsonParser.parseMap(entity.getProviderConfigurationMetadata()));

        if (entity.getClientAuthenticationMethod() != null && !entity.getClientAuthenticationMethod().isBlank()) {
            builder.clientAuthenticationMethod(new ClientAuthenticationMethod(entity.getClientAuthenticationMethod()));
        }
        if (entity.getClientAuthorizationGrantType() != null && !entity.getClientAuthorizationGrantType().isBlank()) {
            builder.authorizationGrantType(new AuthorizationGrantType(entity.getClientAuthorizationGrantType()));
        }
        if (entity.getProviderUserInfoEndpointAuthenticationMethod() != null && !entity.getProviderUserInfoEndpointAuthenticationMethod().isBlank()) {
            builder.userInfoAuthenticationMethod(new AuthenticationMethod(entity.getProviderUserInfoEndpointAuthenticationMethod()));
        }

        return builder.build();
    }

    public Oauth2ClientRegistrationEntity toEntity(ClientRegistration clientRegistration) {
        var entity = new Oauth2ClientRegistrationEntity();
        entity.setClientRegistrationId(clientRegistration.getRegistrationId());
        entity.setClientId(clientRegistration.getClientId());
        entity.setClientSecret(clientRegistration.getClientSecret());
        entity.setClientAuthenticationMethod(clientRegistration.getClientAuthenticationMethod() != null ? clientRegistration.getClientAuthenticationMethod().getValue() : null);
        entity.setClientAuthorizationGrantType(clientRegistration.getAuthorizationGrantType() != null ? clientRegistration.getAuthorizationGrantType().getValue() : null);
        entity.setClientRedirectUri(clientRegistration.getRedirectUri());
        entity.setClientScopes(convertScopes(clientRegistration.getScopes()));
        entity.setClientName(clientRegistration.getClientName());
        entity.setProviderAuthorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri());
        entity.setProviderTokenUri(clientRegistration.getProviderDetails().getTokenUri());
        entity.setProviderUserInfoEndpointUri(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri());
        entity.setProviderUserInfoEndpointAuthenticationMethod(clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod() != null ? clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod().getValue() : null);
        entity.setProviderUserInfoEndpointUserNameAttributeName(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
        entity.setProviderJwkSetUri(clientRegistration.getProviderDetails().getJwkSetUri());
        entity.setProviderIssuerUri(clientRegistration.getProviderDetails().getIssuerUri());
        entity.setProviderConfigurationMetadata(jsonParser.writeMap(clientRegistration.getProviderDetails().getConfigurationMetadata()));
        return entity;
    }

    private Collection<String> convertScopes(String clientScopes) {
        if (clientScopes == null || clientScopes.isBlank()) {
            return Set.of();
        }

        return Set.of(clientScopes.split(","));
    }

    private String convertScopes(Collection<String> clientScopes) {
        if (clientScopes == null || clientScopes.isEmpty()) {
            return null;
        }

        return String.join(",", clientScopes);
    }
}
