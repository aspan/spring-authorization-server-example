package com.example.auth;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.time.Duration;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.ui.DefaultResourcesFilter;
import org.springframework.util.StringUtils;

/**
 * The spring security configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Password encoder
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * The authorization server security filter chain
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception on error
     */
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

    /**
     * The default security filter chain used for logging in users
     *
     * @param authenticationSuccessHandler AuthenticationSuccessHandler
     * @param http                         HttpSecurity
     * @param rememberMeServices           PersistentTokenBasedRememberMeServices
     * @return SecurityFilterChain
     * @throws Exception on error
     */
    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(
            AuthenticationSuccessHandler authenticationSuccessHandler,
            HttpSecurity http,
            OneTimeTokenService oneTimeTokenService,
            PersistentTokenBasedRememberMeServices rememberMeServices) throws Exception {
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
                .formLogin(
                        formLogin ->
                                formLogin.loginPage("/login").permitAll()
                                         .successHandler(authenticationSuccessHandler))
                .logout(logout ->
                                logout.deleteCookies("JSESSIONID",
                                                     "remember-me"))
                .oneTimeTokenLogin(
                        ott -> ott.loginPage("/login")
                                  .loginProcessingUrl("/login/ott")
                                  .showDefaultSubmitPage(false)
                                  .successHandler(authenticationSuccessHandler)
                                  .tokenService(oneTimeTokenService))
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeServices(rememberMeServices)
                        .authenticationSuccessHandler(authenticationSuccessHandler))
                .webAuthn(webAuthn ->
                                  webAuthn
                                          .rpName("Spring Security Relying Party")
                                          .rpId("localhost")
                                          .allowedOrigins("http://localhost:9000")
                                          .disableDefaultRegistrationPage(true)
                )
                .build();
    }

    /**
     * An oauth2 token customizer that adds roles as claims
     *
     * @return OAuth2TokenCustomizer<JwtEncodingContext>
     */
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

    /**
     * The authentication success handler to use for all login methods
     *
     * @return AuthenticationSuccessHandler
     */
    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler();
    }

    /**
     * Remember me authentication RememberMeServices
     *
     * @param userDetailsService        UsesDetailsService
     * @param persistentTokenRepository PersistentTokenRepository
     * @return PersistentTokenBasedRememberMeServices
     */
    @Bean
    PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices(UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository) {
        var service = new PersistentTokenBasedRememberMeServices("rememberMeKey", userDetailsService, persistentTokenRepository);
        service.setTokenValiditySeconds((int) Duration.ofDays(180).getSeconds());
        return service;
    }

    /**
     * An OIDC logout success handler that also removes the remember-me cookie.
     */
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