package com.example.security.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.security.persistence.repository.Oauth2RegisteredClientEntityRepository;

@SpringBootTest
@Disabled
public class PersistenceTest {
    @Autowired
    Oauth2RegisteredClientEntityRepository oauth2RegisteredClientEntityRepository;

    @Test
    void contextLoads() {
        assertThat(oauth2RegisteredClientEntityRepository).isNotNull();
    }
}
