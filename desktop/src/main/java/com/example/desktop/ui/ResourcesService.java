package com.example.desktop.ui;

import java.util.List;

import org.springframework.web.service.annotation.GetExchange;

public interface ResourcesService {
    @GetExchange("/resources")
    List<String> getResources();
}
