package com.example.hilla.services;

import java.util.Optional;

import com.example.hilla.security.AuthenticatedUser;
import com.example.hilla.security.User;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;

@Endpoint
@AnonymousAllowed
public class UserService {

    private final AuthenticatedUser authenticatedUser;

    public UserService(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public Optional<User> getAuthenticatedUser() {
        return authenticatedUser.get();
    }
}
