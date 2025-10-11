package com.example.security.server.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.security.server.persistence.entity.Oauth2ClientRegistrationEntity;

public interface Oauth2ClientRegistrationEntityRepository extends JpaRepository<Oauth2ClientRegistrationEntity, String> {
}
