package com.example.vaadin.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.vaadin.security.AuthenticatedUser;
import com.example.vaadin.security.User;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;

@Endpoint
@AnonymousAllowed
public class UserEndpoint {

    @Autowired
    private AuthenticatedUser authenticatedUser;

    public Optional<User> getAuthenticatedUser() {
        return authenticatedUser.get();
    }
}
