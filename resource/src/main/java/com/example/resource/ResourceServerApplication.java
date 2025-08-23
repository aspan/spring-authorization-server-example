package com.example.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ResourceServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServerApplication.class);
    private final ResourceRepository resourceRepository;

    public ResourceServerApplication(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

    @GetMapping("/resources")
    @PreAuthorize("hasAuthority('SCOPE_resources.read')")
    public String[] getResources(JwtAuthenticationToken authentication) {
        LOGGER.info("Logged in user: {}", authentication.getName());
        return resourceRepository.findAll().stream().map(Resource::getName).toArray(String[]::new);
    }

    @Bean
    CommandLineRunner init(ResourceRepository resourceRepository) {
        return args -> {
            if (resourceRepository.count() == 0L) {
                resourceRepository.saveAll(List.of(new Resource("Resource 1"), new Resource("Resource 2"), new Resource("Resource 3"), new Resource("Resource 4")));
            }
        };
    }
}

