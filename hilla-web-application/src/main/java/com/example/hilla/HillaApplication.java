package com.example.hilla;

import java.io.Serial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;

@SpringBootApplication
public class HillaApplication implements AppShellConfigurator {
    @Serial
    private static final long serialVersionUID = 1L;

    static void main(String[] args) throws Exception {
        SpringApplication.run(HillaApplication.class, args);
    }
}

