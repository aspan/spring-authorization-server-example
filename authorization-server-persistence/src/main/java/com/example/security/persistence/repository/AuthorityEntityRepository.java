package com.example.security.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.security.persistence.entity.AuthorityEntity;

public interface AuthorityEntityRepository extends CrudRepository<AuthorityEntity, Long> {
}
