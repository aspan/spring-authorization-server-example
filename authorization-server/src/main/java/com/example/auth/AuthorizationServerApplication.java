package com.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthorizationServerApplication {
    static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

//    @Bean
//    CommandLineRunner init(RegisteredClientRepository registeredClientRepository, JdbcOperations jdbcOperations) {
//        return args -> {
//            JdbcRegisteredClientRepository jdbcRegisteredClientRepository = new JdbcRegisteredClientRepository(jdbcOperations);
//            for (var r : List.of("desktop-client", "hilla-client", "vaadin-client", "web-client")) {
//                var registeredClient = registeredClientRepository.findById(r);
//                if (registeredClient != null) {
//                    jdbcRegisteredClientRepository.save(registeredClient);
//                }
//            }
//
//
//        };
//    }
}
