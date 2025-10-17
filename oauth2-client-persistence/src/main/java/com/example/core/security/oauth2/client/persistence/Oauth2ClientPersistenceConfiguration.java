package com.example.core.security.oauth2.client.persistence;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration(proxyBeanMethods = false)
@ComponentScan
@EnableJpaRepositories
@EntityScan
public class Oauth2ClientPersistenceConfiguration {
}
