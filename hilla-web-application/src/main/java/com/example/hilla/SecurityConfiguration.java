package com.example.hilla;

import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfiguration {
    private static final String LOGIN_URL = "/login";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.with(
                VaadinSecurityConfigurer.vaadin()
                                        .oauth2LoginPage(LOGIN_URL), configurer -> {
                }).build();
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
}