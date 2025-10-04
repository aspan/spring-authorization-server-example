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
public class AuthorizationService {
    private ConfigurableApplicationContext applicationContext;
    private final CompletableFuture<String> url = new CompletableFuture<>();

    public CompletableFuture<Authentication> authenticate() {
        if (this.applicationContext == null) {
            throw new IllegalStateException("applicationContext is null");
        }
        return this.applicationContext.getBean(CompletableFutureSecurityContextHolderStrategy.class).getAuthenticationFuture();
    }

    public CompletableFuture<String> start(OAuth2AuthorizedClientService authorizedClientService) {
        this.applicationContext = new SpringApplicationBuilder(AuthorizationService.class)
                .initializers((ApplicationContextInitializer<GenericApplicationContext>) ac -> {
                    ac.registerBean(OAuth2AuthorizedClientService.class, () -> authorizedClientService);
                })
                .run();
        return this.applicationContext.getBean(AuthorizationService.class).url();
    }

    CompletableFuture<String> url() {
        return this.url;
    }

    public void stop() {
        if (this.applicationContext != null) {
            this.applicationContext.close();
        }
    }
}

