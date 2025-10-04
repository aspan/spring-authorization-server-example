package com.example.vaadin;

import java.io.Serial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.example.core.OpenBrowserConfiguration;
import com.example.resource.client.ResourceServiceConfig;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.page.AppShellConfigurator;

@Import({OpenBrowserConfiguration.class, ResourceServiceConfig.class})
@SpringBootApplication
@CssImport("@vaadin/aura/aura.css")
public class VaadinApplication implements AppShellConfigurator {
    @Serial
    private static final long serialVersionUID = 1L;

    static void main(String[] args) {
        SpringApplication.run(VaadinApplication.class, args);
    }
}

