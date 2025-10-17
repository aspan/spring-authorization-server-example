package com.example.core.security.oauth2.client.persistence.service;

import java.time.Instant;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.core.security.oauth2.client.persistence.entity.Oauth2AuthorizedClientEntity;
import com.example.core.security.oauth2.client.persistence.entity.Oauth2AuthorizedClientEntity.Oauth2AuthorizedClientEntityId;
import com.example.core.security.oauth2.client.persistence.repository.Oauth2AuthorizedClientEntityRepository;

@Service
public class Oauth2AuthorizedClientService implements OAuth2AuthorizedClientService {
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final Oauth2AuthorizedClientEntityRepository oauth2AuthorizedClientEntityRepository;

    public Oauth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository, Oauth2AuthorizedClientEntityRepository oauth2AuthorizedClientEntityRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.oauth2AuthorizedClientEntityRepository = oauth2AuthorizedClientEntityRepository;
    }

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        return fromEntity(this.oauth2AuthorizedClientEntityRepository.findById(new Oauth2AuthorizedClientEntityId(clientRegistrationId, principalName)).orElse(null));
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        var existing = oauth2AuthorizedClientEntityRepository.findById(new Oauth2AuthorizedClientEntityId(authorizedClient.getClientRegistration().getRegistrationId(), principal.getName()));
        Oauth2AuthorizedClientEntity oauth2AuthorizedClientEntity;
        if (existing.isPresent()) {
            oauth2AuthorizedClientEntity = toEntity(existing.get(), authorizedClient);
        } else {
            oauth2AuthorizedClientEntity = toEntity(new Oauth2AuthorizedClientEntity(), authorizedClient);
            oauth2AuthorizedClientEntity.setCreatedAtTimestamp(Instant.now());
            oauth2AuthorizedClientEntity.setPrincipalName(principal.getName());
        }
        this.oauth2AuthorizedClientEntityRepository.save(oauth2AuthorizedClientEntity);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        this.oauth2AuthorizedClientEntityRepository.deleteById(new Oauth2AuthorizedClientEntityId(clientRegistrationId, principalName));
    }

    private <T extends OAuth2AuthorizedClient> T fromEntity(Oauth2AuthorizedClientEntity entity) {
        if (entity == null) {
            return null;
        }
        var clientRegistration = this.clientRegistrationRepository.findByRegistrationId(entity.getClientRegistrationId());
        if (clientRegistration == null) {
            throw new RuntimeException("clientRegistration not found");
        }

        OAuth2AccessToken.TokenType tokenType = null;
        if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(entity.getAccessTokenType())) {
            tokenType = OAuth2AccessToken.TokenType.BEARER;
        }
        var tokenValue = entity.getAccessTokenValue();
        var issuedAt = entity.getAccessTokenIssuedAt();
        var expiresAt = entity.getAccessTokenExpiresAt();
        Set<String> scopes = Set.of();
        var accessTokenScopes = entity.getAccessTokenScopes();
        if (accessTokenScopes != null) {
            scopes = StringUtils.commaDelimitedListToSet(accessTokenScopes);
        }
        var accessToken = new OAuth2AccessToken(tokenType, tokenValue, issuedAt, expiresAt, scopes);
        OAuth2RefreshToken refreshToken = null;
        if (entity.getRefreshTokenValue() != null && entity.getRefreshTokenIssuedAt() != null) {
            refreshToken = new OAuth2RefreshToken(entity.getRefreshTokenValue(), entity.getRefreshTokenIssuedAt());
        }

        var principalName = entity.getPrincipalName();
        return (T) new OAuth2AuthorizedClient(clientRegistration, principalName, accessToken, refreshToken);
    }

    private static Oauth2AuthorizedClientEntity toEntity(Oauth2AuthorizedClientEntity entity, OAuth2AuthorizedClient authorizedClient) {
        if (authorizedClient == null) {
            return null;
        }
        entity.setClientRegistrationId(authorizedClient.getClientRegistration().getRegistrationId());
        entity.setPrincipalName(authorizedClient.getPrincipalName());
        entity.setAccessTokenScopes(StringUtils.collectionToCommaDelimitedString(authorizedClient.getAccessToken().getScopes()));
        entity.setAccessTokenType(authorizedClient.getAccessToken().getTokenType().getValue());
        entity.setAccessTokenValue(authorizedClient.getAccessToken().getTokenValue());
        entity.setAccessTokenExpiresAt(authorizedClient.getAccessToken().getExpiresAt());
        entity.setAccessTokenIssuedAt(authorizedClient.getAccessToken().getIssuedAt());
        entity.setRefreshTokenValue(authorizedClient.getRefreshToken() != null ? authorizedClient.getRefreshToken().getTokenValue() : null);
        entity.setRefreshTokenIssuedAt(authorizedClient.getRefreshToken() != null ? authorizedClient.getRefreshToken().getIssuedAt() : null);
        return entity;
    }
}
