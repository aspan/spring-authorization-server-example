package com.example.vaadin;

import java.io.Serial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.example.core.OpenBrowserConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    // TODO Remove when upgrading to jackson 3
    @Bean
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder();
    }

    @Bean
    ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.build();
    }

    @Bean
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}

