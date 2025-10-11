package com.example.security.server.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.security.server.persistence.entity.AuthorityEntity;

public interface AuthorityEntityRepository extends CrudRepository<AuthorityEntity, Long> {
}
