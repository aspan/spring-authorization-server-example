package com.example.auth;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.time.Duration;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.ui.DefaultResourcesFilter;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthentication;
import org.springframework.security.web.webauthn.management.JdbcPublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.JdbcUserCredentialRepository;
import org.springframework.util.StringUtils;

import com.example.auth.jackson.ImmutablePublicKeyCredentialUserEntityMixIn;
import com.example.auth.jackson.WebAuthnAuthenticationMixIn;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer().oidc(
                oidcConfigurer -> oidcConfigurer.logoutEndpoint(
                        oidcLogoutEndpointConfigurer -> oidcLogoutEndpointConfigurer
                                .logoutResponseHandler(new RememberMeOidcLogoutSuccessHandler())
                )
        );
        return http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, withDefaults())
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
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices, AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
        return http
                .addFilter(DefaultResourcesFilter.webauthn())
                .authorizeHttpRequests(a -> a
                        .requestMatchers(
                                "/apple-touch-icon.png",
                                "/apple-touch-icon-precomposed.png",
                                "/css/*.css",
                                "/login/**",
                                "/oauth2/**",
                                "/index.html",
                                "/favicon.ico",
                                "/error",
                                "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .webAuthn(webAuthn ->
                                  webAuthn
                                          .rpName("Spring Security Relying Party")
                                          .rpId("localhost")
                                          .allowedOrigins("http://localhost:9000")
                                          .disableDefaultRegistrationPage(true)
                )
                .formLogin(
                        formLogin ->
                                formLogin.loginPage("/login").permitAll()
                                         .successHandler(authenticationSuccessHandler))
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeServices(persistentTokenBasedRememberMeServices)
                        .authenticationSuccessHandler(authenticationSuccessHandler))
                .logout(logout -> logout.deleteCookies("JSESSIONID", "remember-me"))
                .build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oauth2TokenCustomizer() {
        return (context) -> {
            var principal = context.getPrincipal();
            var authorities = principal.getAuthorities().stream()
                                       .map(GrantedAuthority::getAuthority)
                                       .map(role -> role.replace("ROLE_", ""))
                                       .collect(Collectors.toSet());
            context.getClaims().claim("roles", authorities);
        };
    }

    @Bean
    RegisteredClientRepository registeredClientRepository(JdbcOperations jdbcOperations) {
        return new JdbcRegisteredClientRepository(jdbcOperations);
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

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler();
    }

    @Bean
    PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices(UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository) {
        var service = new PersistentTokenBasedRememberMeServices("rememberMeKey", userDetailsService, persistentTokenRepository);
        service.setTokenValiditySeconds((int) Duration.ofDays(180).getSeconds());
        return service;
    }

    @Bean
    PersistentTokenRepository persistentTokenRepository(JdbcTemplate jdbcTemplate) {
        var repository = new JdbcTokenRepositoryImpl();
        repository.setJdbcTemplate(jdbcTemplate);
        return repository;
    }

    static class RememberMeOidcLogoutSuccessHandler implements AuthenticationSuccessHandler {
        private final OidcLogoutAuthenticationSuccessHandler authenticationSuccessHandler = new OidcLogoutAuthenticationSuccessHandler();

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            deleteRememberMeCookie(request, response);
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        }

        void deleteRememberMeCookie(HttpServletRequest request, HttpServletResponse response) {
            var cookie = new Cookie("remember-me", null);
            var contextPath = request.getContextPath();
            var cookiePath = StringUtils.hasText(contextPath) ? contextPath : "/";
            cookie.setPath(cookiePath);
            cookie.setMaxAge(0);
            cookie.setSecure(request.isSecure());
            response.addCookie(cookie);
        }
    }
}