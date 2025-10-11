package com.example.security.server.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.security.server.persistence.repository.Oauth2RegisteredClientEntityRepository;

@SpringBootTest
public class PersistenceIT {
    @Autowired
    Oauth2RegisteredClientEntityRepository oauth2RegisteredClientEntityRepository;

    @Test
    void contextLoads() {
        assertThat(oauth2RegisteredClientEntityRepository).isNotNull();
    }
}
