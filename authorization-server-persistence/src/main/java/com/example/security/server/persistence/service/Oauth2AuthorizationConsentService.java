package com.example.security.server.persistence.service;

import java.util.HashSet;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.example.security.server.persistence.entity.Oauth2AuthorizationConsentEntity;
import com.example.security.server.persistence.repository.Oauth2AuthorizationConsentEntityRepository;

@Service
public class Oauth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
    private final Oauth2AuthorizationConsentEntityRepository oauth2AuthorizationConsentEntityRepository;
    private final RegisteredClientRepository registeredClientRepository;

    public Oauth2AuthorizationConsentService(Oauth2AuthorizationConsentEntityRepository oauth2AuthorizationConsentEntityRepository, RegisteredClientRepository registeredClientRepository) {
        Assert.notNull(oauth2AuthorizationConsentEntityRepository, "oauth2AuthorizationConsentEntityRepository cannot be null");
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        this.oauth2AuthorizationConsentEntityRepository = oauth2AuthorizationConsentEntityRepository;
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        this.oauth2AuthorizationConsentEntityRepository.save(toEntity(authorizationConsent));
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        this.oauth2AuthorizationConsentEntityRepository.deleteByRegisteredClientIdAndPrincipalName(
                authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return this.oauth2AuthorizationConsentEntityRepository.findByRegisteredClientIdAndPrincipalName(
                registeredClientId, principalName).map(this::fromEntity).orElse(null);
    }

    private OAuth2AuthorizationConsent fromEntity(Oauth2AuthorizationConsentEntity oauth2AuthorizationConsentEntity) {
        var registeredClientId = oauth2AuthorizationConsentEntity.getRegisteredClientId();
        var registeredClient = this.registeredClientRepository.findById(registeredClientId);
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '" + registeredClientId + "' was not found in the RegisteredClientRepository.");
        }

        var builder = OAuth2AuthorizationConsent.withId(
                registeredClientId, oauth2AuthorizationConsentEntity.getPrincipalName());
        if (oauth2AuthorizationConsentEntity.getAuthorities() != null) {
            for (var authority : StringUtils.commaDelimitedListToSet(oauth2AuthorizationConsentEntity.getAuthorities())) {
                builder.authority(new SimpleGrantedAuthority(authority));
            }
        }

        return builder.build();
    }

    private Oauth2AuthorizationConsentEntity toEntity(OAuth2AuthorizationConsent authorizationConsent) {
        var entity = new Oauth2AuthorizationConsentEntity();
        entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
        entity.setPrincipalName(authorizationConsent.getPrincipalName());

        var authorities = new HashSet<String>();
        for (var authority : authorizationConsent.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));

        return entity;
    }
}
