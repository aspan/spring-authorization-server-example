package com.example.desktop.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;

import com.example.desktop.auth.AuthenticationServerApplication;

@Component
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private final ExecutorService executorService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public AuthenticationService(ExecutorService executorService, OAuth2AuthorizedClientService authorizedClientService) {
        this.executorService = executorService;
        this.authorizedClientService = authorizedClientService;
    }

    public void authenticate(Consumer<String> loginUrlHandler, Consumer<Authentication> authenticationSuccessHandler, Runnable failureHandler) {
        var securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        var authServerApplication = new AuthenticationServerApplication();
        try {
            var url = authServerApplication.start(this.authorizedClientService).get(10, TimeUnit.SECONDS);
            loginUrlHandler.accept(url);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            failureHandler.run();
            authServerApplication.stop();
            return;
        }
        var authenticationFuture = authServerApplication.authenticate();
        this.executorService.submit(() -> {
            try {
                var authentication = authenticationFuture.get(1, TimeUnit.MINUTES);
                Thread.sleep(10L);
                authServerApplication.stop();
                SecurityContextHolder.setContextHolderStrategy(securityContextHolderStrategy);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                LOGGER.debug("Authentication completed {}", SecurityContextHolder.getContext().getAuthentication());
                authenticationSuccessHandler.accept(authentication);
            } catch (Exception e) {
                LOGGER.error("Login failed with exception", e);
                authServerApplication.stop();
                failureHandler.run();
            }
        });
    }
}
