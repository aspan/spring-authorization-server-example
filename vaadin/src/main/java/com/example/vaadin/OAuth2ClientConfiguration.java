package com.example.vaadin;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientProperties;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientPropertiesMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.web.client.SecurityContextHolderPrincipalResolver;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(OAuth2ClientProperties.class)
public class OAuth2ClientConfiguration {

    @Bean
    ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oAuth2ClientProperties) {
        var clientRegistrations = List.copyOf(
                new OAuth2ClientPropertiesMapper(oAuth2ClientProperties)
                        .asClientRegistrations()
                        .values()
        );
        return new InMemoryClientRegistrationRepository(clientRegistrations);

    }

    @Bean
    OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository, JdbcOperations jdbcOperations) {
        return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
    }

    @Bean
    OAuth2AuthorizedClientRepository authorizedClientRepository(OAuth2AuthorizedClientService authorizedClientService) {
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
    }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {
        var authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(
                OAuth2AuthorizedClientProviderBuilder.builder()
                                                     .authorizationCode()
                                                     .refreshToken()
                                                     .build());
        return authorizedClientManager;
    }

    @Bean
    OAuth2ClientHttpRequestInterceptor oAuth2ClientHttpRequestInterceptor(
            OAuth2AuthorizedClientManager authorizedClientManager,
            SecurityContextHolderStrategy securityContextHolderStrategy,
            Environment environment) {
        var interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
        interceptor.setClientRegistrationIdResolver(request -> environment.getProperty("example.client-registration-id", "vaadin-client"));
        interceptor.setPrincipalResolver(new SecurityContextHolderPrincipalResolver(securityContextHolderStrategy));
        return interceptor;
    }

    @Bean
    RestClient restClient(
            OAuth2ClientHttpRequestInterceptor interceptor,
            Environment environment) {
        return RestClient.builder()
                         .baseUrl(environment.getProperty("example.resource-server-url", "http://127.0.0.1:8090"))
                         .requestInterceptor(
                                 interceptor
                         )
                         .build();
    }
}