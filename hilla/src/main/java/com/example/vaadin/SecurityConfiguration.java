package com.example.vaadin;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.vaadin.flow.spring.security.UidlRedirectStrategy;
import com.vaadin.flow.spring.security.VaadinWebSecurity;

@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {
    private static final String LOGIN_URL = "/login";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setOAuth2LoginPage(http, LOGIN_URL);
    }
}