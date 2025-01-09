package com.example.vaadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@Theme(value = "my-app")
public class HillaApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(HillaApplication.class, args);
    }
}

