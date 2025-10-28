package com.example.security.server.persistence;

import java.util.TreeSet;

import org.hibernate.collection.spi.PersistentSet;
import org.hibernate.collection.spi.PersistentSortedSet;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.ott.OneTimeTokenAuthentication;
import org.springframework.security.jackson.SecurityJacksonModules;
import org.springframework.security.oauth2.server.authorization.jackson.OAuth2AuthorizationServerJacksonModule;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthentication;

import com.example.security.server.persistence.jackson.ImmutablePublicKeyCredentialUserEntityMixIn;
import com.example.security.server.persistence.jackson.OneTimeTokenAuthenticationMixIn;
import com.example.security.server.persistence.jackson.SetMixin;
import com.example.security.server.persistence.jackson.WebAuthnAuthenticationMixIn;

import tools.jackson.databind.json.JsonMapper.Builder;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration(proxyBeanMethods = false)
@ComponentScan
@EnableScheduling
@EnableJpaRepositories
@EntityScan
public class SecurityPersistenceConfiguration {
    @Bean
    JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
        return new JsonMapperBuilderCustomizer() {
            @Override
            public void customize(@NonNull Builder jsonMapperBuilder) {
                BasicPolymorphicTypeValidator.Builder builder = BasicPolymorphicTypeValidator.builder();
                var oAuth2AuthorizationServerJacksonModule = new OAuth2AuthorizationServerJacksonModule();
                oAuth2AuthorizationServerJacksonModule.configurePolymorphicTypeValidator(builder);
                jsonMapperBuilder.addModules(oAuth2AuthorizationServerJacksonModule)
                                 .addModules(SecurityJacksonModules.getModules(getClass().getClassLoader(), builder))
                                 .addMixIn(OneTimeTokenAuthentication.class, OneTimeTokenAuthenticationMixIn.class)
                                 .addMixIn(ImmutablePublicKeyCredentialUserEntity.class, ImmutablePublicKeyCredentialUserEntityMixIn.class)
                                 .addMixIn(WebAuthnAuthentication.class, WebAuthnAuthenticationMixIn.class)
                                 .addMixIn(PersistentSortedSet.class, SetMixin.class)
                                 .addMixIn(PersistentSet.class, SetMixin.class)
                                 .addMixIn(TreeSet.class, SetMixin.class);
            }
        };
    }
}
