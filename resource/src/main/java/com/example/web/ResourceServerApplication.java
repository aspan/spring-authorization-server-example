package com.example.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ResourceServerApplication {
private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServerApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

    @GetMapping("/resources")
    @PreAuthorize("hasAuthority('SCOPE_resources.read')")
    public String[] getResources(JwtAuthenticationToken authentication) {
        LOGGER.info("Logged in user: {}", authentication.getName());
        return new String[] { "Resource 1", "Resource 2", "Resource 3" };
    }
}

