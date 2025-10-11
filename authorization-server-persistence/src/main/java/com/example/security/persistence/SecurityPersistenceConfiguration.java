package com.example.security.persistence;

import java.util.TreeSet;

import org.hibernate.collection.spi.PersistentSet;
import org.hibernate.collection.spi.PersistentSortedSet;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.ott.OneTimeTokenAuthentication;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthentication;

import com.example.security.persistence.jackson.ImmutablePublicKeyCredentialUserEntityMixIn;
import com.example.security.persistence.jackson.OneTimeTokenAuthenticationMixIn;
import com.example.security.persistence.jackson.SetMixin;
import com.example.security.persistence.jackson.WebAuthnAuthenticationMixIn;
import com.example.security.persistence.service.Oauth2AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration(proxyBeanMethods = false)
@ComponentScan
@EnableScheduling
@EnableJpaRepositories
@EntityScan
public class SecurityPersistenceConfiguration {
    @Bean
    ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        var classLoader = Oauth2AuthorizationService.class.getClassLoader();
        var securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.addMixIn(OneTimeTokenAuthentication.class, OneTimeTokenAuthenticationMixIn.class);
        objectMapper.addMixIn(ImmutablePublicKeyCredentialUserEntity.class, ImmutablePublicKeyCredentialUserEntityMixIn.class);
        objectMapper.addMixIn(WebAuthnAuthentication.class, WebAuthnAuthenticationMixIn.class);
        objectMapper.addMixIn(PersistentSortedSet.class, SetMixin.class);
        objectMapper.addMixIn(PersistentSet.class, SetMixin.class);
        objectMapper.addMixIn(TreeSet.class, SetMixin.class);
        return objectMapper;
    }
}
