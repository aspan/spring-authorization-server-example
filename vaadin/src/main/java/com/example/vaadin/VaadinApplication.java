package com.example.vaadin;

import java.io.Serial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.page.AppShellConfigurator;

@SpringBootApplication
@CssImport("@vaadin/aura/aura.css")
public class VaadinApplication implements AppShellConfigurator {
    @Serial
    private static final long serialVersionUID = 1L;

    static void main(String[] args) {
        SpringApplication.run(VaadinApplication.class, args);
    }
}

