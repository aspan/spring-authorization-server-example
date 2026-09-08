package com.example.hilla.security;

import java.util.Optional;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

import com.vaadin.flow.spring.security.AuthenticationContext;

@Component
public class AuthenticatedUser {
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public Optional<User> get() {
        return this.authenticationContext.getAuthenticatedUser(DefaultOidcUser.class)
                                         .map(oidcUser -> new User(oidcUser.getSubject()));
    }
}
