package com.example.web;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.core.security.oauth2.client.JdbcClientRegistrationRepository;

@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityContextHolderStrategy securityContextHolderStrategy() {
        return SecurityContextHolder.getContextHolderStrategy();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        var requestHandler = new CsrfTokenRequestAttributeHandler();
        // set the name of the attribute the CsrfToken will be populated on
        requestHandler.setCsrfRequestAttributeName(null);
        return http
                .authorizeHttpRequests(a -> a
                        .requestMatchers(
                                "/",
                                "/apple-touch-icon.png",
                                "/apple-touch-icon-precomposed.png",
                                "/login/**",
                                "/oauth2/**",
                                "/index.html",
                                "/favicon.ico",
                                "/error",
                                "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .csrf(c -> c
                        .csrfTokenRequestHandler(requestHandler)
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .logout(l -> l
                        .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
                )
                .oauth2Login(withDefaults())
                .build();
    }

    @Bean
    @SuppressWarnings("unchecked")
    GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            var mappedAuthorities = new HashSet<GrantedAuthority>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority oidcAuth) {
                    oidcAuth.getIdToken()
                            .getClaimAsStringList("roles")
                            .forEach(role -> mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role)));

                } else if (authority instanceof OAuth2UserAuthority oauth2Auth) {
                    var rolesAttribute = oauth2Auth.getAttributes().get("roles");
                    if (rolesAttribute != null) {
                        ((List<String>) rolesAttribute).forEach(
                                role -> mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role))
                        );
                    }
                }
            });

            return mappedAuthorities;
        };
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository(JdbcOperations jdbcOperations) {
        return new JdbcClientRegistrationRepository(jdbcOperations);
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        // Sets the location that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");

        return oidcLogoutSuccessHandler;
    }

    private static final class CsrfCookieFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            var csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            // Render the token value to a cookie by causing the deferred token to be loaded
            csrfToken.getToken();

            filterChain.doFilter(request, response);
        }
    }
}
