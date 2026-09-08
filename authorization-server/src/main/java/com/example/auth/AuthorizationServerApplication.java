package com.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.example.security.server.persistence.SecurityPersistenceConfiguration;

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
