package com.example.desktop.auth;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(a -> a
                        .requestMatchers(
                                new AntPathRequestMatcher("/apple-touch-icon.png"),
                                new AntPathRequestMatcher("/apple-touch-icon-precomposed.png"),
                                new AntPathRequestMatcher("/login/**"),
                                new AntPathRequestMatcher("/oauth2/**"),
                                new AntPathRequestMatcher("/favicon.ico"),
                                new AntPathRequestMatcher("/error")).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults())
                .build();
    }
}
