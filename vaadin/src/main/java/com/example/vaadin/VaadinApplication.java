package com.example.vaadin;

import java.io.Serial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.example.core.OpenBrowserConfiguration;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.page.AppShellConfigurator;

@Import(OpenBrowserConfiguration.class)
@SpringBootApplication
@CssImport("@vaadin/vaadin-lumo-styles/lumo.css")
public class VaadinApplication implements AppShellConfigurator {
    @Serial
    private static final long serialVersionUID = 1L;

    static void main(String[] args) {
        SpringApplication.run(VaadinApplication.class, args);
    }
}

