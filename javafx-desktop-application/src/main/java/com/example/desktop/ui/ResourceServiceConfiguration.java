package com.example.desktop.ui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.example.core.security.oauth2.client.JdbcClientRegistrationRepository;
import com.example.resource.client.ResourceService;

@Configuration
public class ResourceServiceConfiguration {
    @Bean
    OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository, JdbcOperations jdbcOperations) {
        return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
    }

    @Bean
    ResourceService resourcesService(ClientRegistrationRepository clientRegistrationRepository,
                                     OAuth2AuthorizedClientService authorizedClientService) {
        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(
                OAuth2AuthorizedClientProviderBuilder.builder()
                                                     .authorizationCode()
                                                     .refreshToken()
                                                     .build());

        var interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
        interceptor.setClientRegistrationIdResolver(request -> "desktop-client");

        return HttpServiceProxyFactory.builderFor(
                                              RestClientAdapter.create(
                                                      RestClient.builder()
                                                                .baseUrl("http://127.0.0.1:8090")
                                                                .requestInterceptor(
                                                                        interceptor
                                                                )
                                                                .build()
                                              ))
                                      .build()
                                      .createClient(ResourceService.class);
    }
}