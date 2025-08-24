package com.example.desktop.auth;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import com.example.core.security.oauth2.client.JdbcClientRegistrationRepository;

@Configuration
public class SecurityConfiguration {
    @Bean
    ClientRegistrationRepository clientRegistrationRepository(JdbcOperations jdbcOperations) {
        return new JdbcClientRegistrationRepository(jdbcOperations);
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(a -> a
                        .requestMatchers(
                                "/apple-touch-icon.png",
                                "/apple-touch-icon-precomposed.png",
                                "/login/**",
                                "/oauth2/**",
                                "/favicon.ico",
                                "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults())
                .build();
    }
}
