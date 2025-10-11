package com.example.security.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.security.persistence.entity.Oauth2AuthorizationEntity;

@Repository
public interface Oauth2AuthorizationEntityRepository extends JpaRepository<Oauth2AuthorizationEntity, String> {
    Optional<Oauth2AuthorizationEntity> findByState(String state);

    Optional<Oauth2AuthorizationEntity> findByAuthorizationCodeValue(String authorizationCode);

    Optional<Oauth2AuthorizationEntity> findByAccessTokenValue(String accessToken);

    Optional<Oauth2AuthorizationEntity> findByRefreshTokenValue(String refreshToken);

    @Query("select a from Oauth2AuthorizationEntity a where a.state = :token" +
           " or a.authorizationCodeValue = :token" +
           " or a.accessTokenValue = :token" +
           " or a.refreshTokenValue = :token"
    )
    Optional<Oauth2AuthorizationEntity> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(@Param("token") String token);

    Optional<Oauth2AuthorizationEntity> findByOidcIdTokenValue(String oidcIdTokenValue);

    Optional<Oauth2AuthorizationEntity> findByUserCodeValue(String userCodeValue);

    Optional<Oauth2AuthorizationEntity> findByDeviceCodeValue(String deviceCodeValue);
}
