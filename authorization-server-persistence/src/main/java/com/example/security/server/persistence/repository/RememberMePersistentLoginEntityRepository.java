package com.example.security.server.persistence.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.security.server.persistence.entity.RememberMePersistentLoginEntity;

public interface RememberMePersistentLoginEntityRepository extends CrudRepository<RememberMePersistentLoginEntity, String> {
    @Transactional
    void deleteByUsername(String username);
}
