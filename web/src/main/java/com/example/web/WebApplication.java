package com.example.web;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.example.core.OpenBrowserConfiguration;

@Import(OpenBrowserConfiguration.class)
@RestController
@SpringBootApplication
public class WebApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebApplication.class);
    private final WebClient webClient;

    public WebApplication(WebClient webClient) {
        this.webClient = webClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @RequestMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("sub"));
    }

    @GetMapping(value = "/resources")
    public String[] getResources(
            @RegisteredOAuth2AuthorizedClient("web-client") OAuth2AuthorizedClient authorizedClient
    ) {
        try {
            return this.webClient
                    .get()
                    .uri("http://127.0.0.1:8090/resources")
                    .attributes(oauth2AuthorizedClient(authorizedClient))
                    .retrieve()
                    .bodyToMono(String[].class)
                    .block();
        } catch (WebClientResponseException e) {
            LOGGER.error("", e);
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new ResponseStatusException(HttpStatusCode.valueOf(500), e.getMessage());
        }
    }
}

