package com.example.desktop.ui;

import java.net.CookieManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;

import com.example.desktop.auth.AuthorizationService;

@Component
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private final CookieManager cookieManager;
    private final ExecutorService executorService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private AuthorizationService authorizationService;
    private Future<?> authenticationTask;

    public AuthenticationService(ExecutorService executorService, OAuth2AuthorizedClientService authorizedClientService, CookieManager cookieManager) {
        this.cookieManager = cookieManager;
        this.executorService = executorService;
        this.authorizedClientService = authorizedClientService;
    }

    public void authenticate(Consumer<String> loginUrlHandler, Consumer<Authentication> authenticationSuccessHandler, Consumer<String> failureHandler) {
        var securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        this.authorizationService = new AuthorizationService();
        try {
            var url = this.authorizationService.start(this.authorizedClientService).get(10, TimeUnit.SECONDS);
            loginUrlHandler.accept(url);
        } catch (Exception e) {
            LOGGER.error("Failed to start authorization service", e);
            failureHandler.accept("Failed to start authorization service");
            this.authorizationService.stop();
            return;
        }
        var authenticationFuture = this.authorizationService.authenticate();
        this.authenticationTask = this.executorService.submit(() -> {
            try {
                var authentication = authenticationFuture.get(1, TimeUnit.MINUTES);
                Thread.sleep(10L);
                this.authorizationService.stop();
                SecurityContextHolder.setContextHolderStrategy(securityContextHolderStrategy);
                setAuthentication(authentication);
                LOGGER.debug("Authentication completed {}", authentication);
                authenticationSuccessHandler.accept(authentication);
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    LOGGER.warn("Login cancelled");
                    failureHandler.accept("Login cancelled");
                } else if (e instanceof TimeoutException) {
                    LOGGER.warn("Login timed out");
                    failureHandler.accept("Login timed out");
                } else {
                    LOGGER.error("Login failed", e);
                    failureHandler.accept("Login failed");
                }
                this.authorizationService.stop();
                SecurityContextHolder.setContextHolderStrategy(securityContextHolderStrategy);
            }
        });
    }

    void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public void logout() {
        SecurityContextHolder.clearContext();
        this.cookieManager.getCookieStore().removeAll();
    }

    public void cancel() {
        if (this.authenticationTask != null) {
            this.authenticationTask.cancel(true);
        }
    }
}
