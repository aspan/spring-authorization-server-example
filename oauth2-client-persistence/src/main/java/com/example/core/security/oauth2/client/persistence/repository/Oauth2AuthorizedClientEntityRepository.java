package com.example.core.security.oauth2.client.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.core.security.oauth2.client.persistence.entity.Oauth2AuthorizedClientEntity;
import com.example.core.security.oauth2.client.persistence.entity.Oauth2AuthorizedClientEntity.Oauth2AuthorizedClientEntityId;

public interface Oauth2AuthorizedClientEntityRepository extends CrudRepository<Oauth2AuthorizedClientEntity, Oauth2AuthorizedClientEntityId> {
}
