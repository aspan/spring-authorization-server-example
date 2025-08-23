package com.example.web.service;

import java.util.List;

import org.springframework.web.service.annotation.GetExchange;

public interface ResourceService {
    @GetExchange("/resources")
    List<String> getResources();
}
