package com.example.hilla.services;

import java.util.List;

import org.springframework.web.service.annotation.GetExchange;

public interface ResourcesRemoteService {
    @GetExchange("/resources")
    List<String> getResources();
}
