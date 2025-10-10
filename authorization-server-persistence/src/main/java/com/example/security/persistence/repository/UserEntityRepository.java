package com.example.security.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.security.persistence.entity.UserEntity;

public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
