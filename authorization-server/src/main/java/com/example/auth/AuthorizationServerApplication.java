package com.example.auth;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import com.example.security.persistence.SecurityPersistenceConfiguration;
import com.example.security.persistence.repository.Oauth2RegisteredClientEntityRepository;
import com.example.security.persistence.service.JsonParser;
import com.example.security.persistence.service.Oauth2RegisteredClientRepository;

@SpringBootApplication
@Import(SecurityPersistenceConfiguration.class)
public class AuthorizationServerApplication {
    static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

//    @Bean
//    CommandLineRunner init(RegisteredClientRepository registeredClientRepository, Oauth2RegisteredClientEntityRepository repository, JsonParser jsonParser) {
//        return args -> {
//            var jdbcRegisteredClientRepository = new Oauth2RegisteredClientRepository(repository, jsonParser);
//            for (var r : List.of("desktop-client", "hilla-client", "vaadin-client", "web-client")) {
//                var registeredClient = registeredClientRepository.findById(r);
//                if (registeredClient != null) {
//                    jdbcRegisteredClientRepository.save(registeredClient);
//                }
//            }
//        };
//    }
}
