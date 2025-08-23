package com.example.hilla.services;

import java.util.List;

import jakarta.annotation.security.PermitAll;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@PermitAll
@Service
public class ResourceEndpointService {
    private final ResourceService resourceService;

    public ResourceEndpointService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public List<String> resources() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return resourceService.getResources();
    }
}
