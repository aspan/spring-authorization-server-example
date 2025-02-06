package com.example.desktop.auth;

import java.util.concurrent.CompletableFuture;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

@SpringBootApplication
public class AuthenticationServerApplication {
    private ConfigurableApplicationContext applicationContext;
    private final CompletableFuture<String> url = new CompletableFuture<>();

    public CompletableFuture<Authentication> authenticate() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext is null");
        }
        return applicationContext.getBean(CompletableFutureSecurityContextHolderStrategy.class).getAuthenticationFuture();
    }

    public CompletableFuture<String> start(OAuth2AuthorizedClientService authorizedClientService) {
        applicationContext = new SpringApplicationBuilder(AuthenticationServerApplication.class)
                .initializers((ApplicationContextInitializer<GenericApplicationContext>) ac -> {
                    ac.registerBean(OAuth2AuthorizedClientService.class, () -> authorizedClientService);
                })
                .run();
        return applicationContext.getBean(AuthenticationServerApplication.class).url();
    }

    CompletableFuture<String> url() {
        return url;
    }

    public void stop() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }
}

