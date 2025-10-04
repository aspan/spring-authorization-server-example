package com.example.resource.client;

import java.util.List;

import org.springframework.web.service.annotation.GetExchange;

public interface ResourceService {
    @GetExchange("/resources")
    List<Resource> getResources();
}
