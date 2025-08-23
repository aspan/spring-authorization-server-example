package com.example.hilla.services;

import java.util.List;

import jakarta.annotation.security.PermitAll;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@PermitAll
@Service
public class ResourcesService {
    private final ResourcesRemoteService resourcesRemoteService;

    public ResourcesService(ResourcesRemoteService resourcesRemoteService) {
        this.resourcesRemoteService = resourcesRemoteService;
    }

    public List<String> resources() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return resourcesRemoteService.getResources();
    }
}
