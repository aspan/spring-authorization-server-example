package com.example.security.persistence.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.example.security.persistence.entity.Oauth2RegisteredClientEntity;
import com.example.security.persistence.repository.Oauth2RegisteredClientEntityRepository;

@Service
public class Oauth2RegisteredClientRepository implements RegisteredClientRepository {
    private final Oauth2RegisteredClientEntityRepository oauth2RegisteredClientEntityRepository;
    private final JsonParser jsonParser;

    public Oauth2RegisteredClientRepository(Oauth2RegisteredClientEntityRepository oauth2RegisteredClientEntityRepository, JsonParser jsonParser) {
        Assert.notNull(oauth2RegisteredClientEntityRepository, "clientRepository cannot be null");
        this.oauth2RegisteredClientEntityRepository = oauth2RegisteredClientEntityRepository;
        this.jsonParser = jsonParser;
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);      // Custom client authentication method
    }

    public static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        this.oauth2RegisteredClientEntityRepository.save(toEntity(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.oauth2RegisteredClientEntityRepository.findById(id).map(this::fromEntity).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return this.oauth2RegisteredClientEntityRepository.findByClientId(clientId).map(this::fromEntity).orElse(null);
    }

    private RegisteredClient fromEntity(Oauth2RegisteredClientEntity oauth2RegisteredClientEntity) {
        var clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(
                oauth2RegisteredClientEntity.getClientAuthenticationMethods());
        var authorizationGrantTypes = StringUtils.commaDelimitedListToSet(
                oauth2RegisteredClientEntity.getAuthorizationGrantTypes());
        var redirectUris = StringUtils.commaDelimitedListToSet(
                oauth2RegisteredClientEntity.getRedirectUris());
        var postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(
                oauth2RegisteredClientEntity.getPostLogoutRedirectUris());
        var clientScopes = StringUtils.commaDelimitedListToSet(
                oauth2RegisteredClientEntity.getScopes());

        var builder = RegisteredClient.withId(oauth2RegisteredClientEntity.getId())
                                      .clientId(oauth2RegisteredClientEntity.getClientId())
                                      .clientIdIssuedAt(oauth2RegisteredClientEntity.getClientIdIssuedAt())
                                      .clientSecret(oauth2RegisteredClientEntity.getClientSecret())
                                      .clientSecretExpiresAt(oauth2RegisteredClientEntity.getClientSecretExpiresAt())
                                      .clientName(oauth2RegisteredClientEntity.getClientName())
                                      .clientAuthenticationMethods(authenticationMethods ->
                                                                           clientAuthenticationMethods.forEach(authenticationMethod ->
                                                                                                                       authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                                      .authorizationGrantTypes((grantTypes) ->
                                                                       authorizationGrantTypes.forEach(grantType ->
                                                                                                               grantTypes.add(resolveAuthorizationGrantType(grantType))))
                                      .redirectUris((uris) -> uris.addAll(redirectUris))
                                      .postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris))
                                      .scopes((scopes) -> scopes.addAll(clientScopes));

        var clientSettingsMap = jsonParser.parseMap(oauth2RegisteredClientEntity.getClientSettings());
        builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

        var tokenSettingsMap = jsonParser.parseMap(oauth2RegisteredClientEntity.getTokenSettings());
        builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());

        return builder.build();
    }

    private Oauth2RegisteredClientEntity toEntity(RegisteredClient registeredClient) {
        var clientAuthenticationMethods = new ArrayList<>(registeredClient.getClientAuthenticationMethods().size());
        registeredClient.getClientAuthenticationMethods().forEach(clientAuthenticationMethod ->
                                                                          clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));

        var authorizationGrantTypes = new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
        registeredClient.getAuthorizationGrantTypes().forEach(authorizationGrantType ->
                                                                      authorizationGrantTypes.add(authorizationGrantType.getValue()));

        var entity = new Oauth2RegisteredClientEntity();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        entity.setPostLogoutRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
        entity.setClientSettings(jsonParser.writeMap(new HashMap<>(registeredClient.getClientSettings().getSettings())));
        entity.setTokenSettings(jsonParser.writeMap(new HashMap<>(registeredClient.getTokenSettings().getSettings())));

        return entity;
    }
}
