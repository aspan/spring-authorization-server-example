package com.example.security.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.security.persistence.entity.Oauth2ClientRegistrationEntity;

public interface Oauth2ClientRegistrationEntityRepository extends JpaRepository<Oauth2ClientRegistrationEntity, String> {
}
