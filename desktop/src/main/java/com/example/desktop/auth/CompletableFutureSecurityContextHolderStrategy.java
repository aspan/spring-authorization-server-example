package com.example.desktop.auth;

import java.util.concurrent.CompletableFuture;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CompletableFutureSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    private SecurityContext securityContext;
    private final CompletableFuture<Authentication> authenticationFuture = new CompletableFuture<>();

    @Override
    public void clearContext() {
        securityContext = null;
    }

    @Override
    public SecurityContext getContext() {
        if (securityContext == null) {
            securityContext = createEmptyContext();
        }
        return securityContext;
    }

    @Override
    public void setContext(SecurityContext context) {
        Assert.notNull(context, "SecurityContext must not be null");
        var authentication = context.getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            authenticationFuture.complete(authentication);
        }
        this.securityContext = context;
    }

    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }

    public CompletableFuture<Authentication> getAuthenticationFuture() {
        return authenticationFuture;
    }
}
