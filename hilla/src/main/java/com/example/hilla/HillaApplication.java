package com.example.hilla;

import java.io.Serial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.example.core.OpenBrowserConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.page.AppShellConfigurator;

@SpringBootApplication
@Import(OpenBrowserConfiguration.class)
public class HillaApplication implements AppShellConfigurator {
    @Serial
    private static final long serialVersionUID = 1L;

    static void main(String[] args) throws Exception {
        SpringApplication.run(HillaApplication.class, args);
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

