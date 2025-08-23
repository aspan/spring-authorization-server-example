package com.example.auth;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthentication;
import org.springframework.security.web.webauthn.management.JdbcPublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.JdbcUserCredentialRepository;

import com.example.auth.jackson.ImmutablePublicKeyCredentialUserEntityMixIn;
import com.example.auth.jackson.WebAuthnAuthenticationMixIn;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        var authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer()
                                                                               .oidc(withDefaults());
        return http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, Customizer.withDefaults())
                .authorizeHttpRequests(
                        a ->
                                a.anyRequest().authenticated()
                )
                .exceptionHandling(
                        e ->
                                e.authenticationEntryPoint(
                                        new LoginUrlAuthenticationEntryPoint("/login")))
                .oauth2ResourceServer(
                        r ->
                                r.jwt(withDefaults()))
                .formLogin(withDefaults()).build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        a ->
                                a.anyRequest().authenticated())
                .webAuthn(webAuthn ->
                                  webAuthn
                                          .rpName("Spring Security Relying Party")
                                          .rpId("localhost")
                                          .allowedOrigins("http://localhost:9000")
                )
                .formLogin(withDefaults())
                .build();
    }

    @Bean
    OAuth2AuthorizationConsentService oauth2AuthorizationConsentService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, registeredClientRepository);
    }

    @Bean
    OAuth2AuthorizationService oauth2AuthorizationService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository) {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModules(SecurityJackson2Modules.getModules(SecurityConfiguration.class.getClassLoader()));
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.addMixIn(WebAuthnAuthentication.class, WebAuthnAuthenticationMixIn.class);
        objectMapper.addMixIn(ImmutablePublicKeyCredentialUserEntity.class, ImmutablePublicKeyCredentialUserEntityMixIn.class);
        var authService = new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
        var rowMapper = new OAuth2AuthorizationRowMapper(registeredClientRepository);
        rowMapper.setObjectMapper(objectMapper);
        authService.setAuthorizationRowMapper(rowMapper);
        return authService;
    }

    @Bean
    JdbcPublicKeyCredentialUserEntityRepository jdbcPublicKeyCredentialRepository(JdbcOperations jdbc) {
        return new JdbcPublicKeyCredentialUserEntityRepository(jdbc);
    }

    @Bean
    JdbcUserCredentialRepository jdbcUserCredentialRepository(JdbcOperations jdbc) {
        return new JdbcUserCredentialRepository(jdbc);
    }
}