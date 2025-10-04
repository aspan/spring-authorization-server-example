package com.example.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.resource.client.Resource;
import com.example.resource.client.ResourceService;

@RestController
@SpringBootApplication
public class WebApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebApplication.class);
    private final ResourceService resourceService;

    public WebApplication(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @RequestMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("sub"));
    }

    @GetMapping(value = "/resources")
    public List<Resource> getResources() {
        try {
            return resourceService.getResources();
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new ResponseStatusException(HttpStatusCode.valueOf(500), e.getMessage());
        }
    }

//    @Bean
//    CommandLineRunner init(ClientRegistrationRepository clientRegistrationRepository, JdbcOperations jdbcOperations) {
//        return args -> {
//            new JdbcClientRegistrationRepository(jdbcOperations).save(clientRegistrationRepository.findByRegistrationId("web-client"));
//        };
//    }
}

