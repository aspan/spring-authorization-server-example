package com.example.hilla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.example.core.OpenBrowserConfiguration;
import com.vaadin.flow.component.page.AppShellConfigurator;

@SpringBootApplication
@Import(OpenBrowserConfiguration.class)
public class HillaApplication implements AppShellConfigurator {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(HillaApplication.class, args);
    }
}

