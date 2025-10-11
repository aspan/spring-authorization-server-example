package com.example.security.server.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.security.server.persistence.entity.UserEntity;

public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
