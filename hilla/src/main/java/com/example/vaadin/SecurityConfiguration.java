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

    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfiguration(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.oauth2Login(c ->
                        c.loginPage(LOGIN_URL).permitAll())
                .logout(logout ->
                        logout.logoutSuccessHandler(logoutSuccessHandler())
                );
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        var oidcLogoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        oidcLogoutSuccessHandler.setRedirectStrategy(new UidlRedirectStrategy());
        return oidcLogoutSuccessHandler;
    }
}