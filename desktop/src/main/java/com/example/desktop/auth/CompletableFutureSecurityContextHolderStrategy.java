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
    private CompletableFuture<Authentication> authenticationFuture;

    @Override
    public void clearContext() {
        this.securityContext = null;
    }

    @Override
    public SecurityContext getContext() {
        if (this.securityContext == null) {
            this.securityContext = createEmptyContext();
        }
        return this.securityContext;
    }

    @Override
    public void setContext(SecurityContext context) {
        Assert.notNull(context, "SecurityContext must not be null");
        var authentication = context.getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            if (this.authenticationFuture != null) {
                this.authenticationFuture.complete(authentication);
                this.authenticationFuture = null;
            }
        }
        this.securityContext = context;
    }

    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }

    public CompletableFuture<Authentication> getAuthenticationFuture() {
        if (this.authenticationFuture == null) {
            this.authenticationFuture = new CompletableFuture<>();
        }
        return this.authenticationFuture;
    }
}
