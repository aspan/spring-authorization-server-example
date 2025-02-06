package com.example.hilla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.example.core.OpenBrowserConfiguration;
import com.example.core.OpenBrowserOnApplicationReadyEventListener;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@Theme(value = "my-app")
@Import(OpenBrowserConfiguration.class)
public class HillaApplication implements AppShellConfigurator {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(HillaApplication.class, args);
    }
}

