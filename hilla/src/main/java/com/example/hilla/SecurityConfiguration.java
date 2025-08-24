package com.example.hilla;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import com.example.core.security.oauth2.client.JdbcClientRegistrationRepository;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfiguration {
    private static final String LOGIN_URL = "/login";

    @Bean
    ClientRegistrationRepository clientRegistrationRepository(JdbcOperations jdbcOperations) {
        return new JdbcClientRegistrationRepository(jdbcOperations);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.with(
                VaadinSecurityConfigurer.vaadin()
                                        .oauth2LoginPage(LOGIN_URL), configurer -> {
                }).build();
    }
}