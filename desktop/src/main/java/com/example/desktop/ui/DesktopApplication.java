package com.example.desktop.ui;

import static org.springframework.security.core.context.SecurityContextHolder.MODE_GLOBAL;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;

import javafx.application.Application;

@SpringBootApplication
public class DesktopApplication {
    public static void main(String[] args) {
        SecurityContextHolder.setStrategyName(MODE_GLOBAL);
        Application.launch(JavaFxApplication.class, args);
    }

    @Bean
    ExecutorService executorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    CookieManager cookieManager() {
        var cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        return cookieManager;
    }
}
