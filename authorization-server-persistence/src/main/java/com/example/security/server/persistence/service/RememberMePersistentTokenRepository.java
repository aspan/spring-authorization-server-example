package com.example.security.server.persistence.service;

import java.util.Date;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import com.example.security.server.persistence.entity.RememberMePersistentLoginEntity;
import com.example.security.server.persistence.repository.RememberMePersistentLoginEntityRepository;

@Service
public class RememberMePersistentTokenRepository implements PersistentTokenRepository {
    private final RememberMePersistentLoginEntityRepository rememberMePersistentLoginEntityRepository;

    public RememberMePersistentTokenRepository(RememberMePersistentLoginEntityRepository rememberMePersistentLoginEntityRepository) {
        this.rememberMePersistentLoginEntityRepository = rememberMePersistentLoginEntityRepository;
    }

    @Override
    public void createNewToken(@NonNull PersistentRememberMeToken token) {
        this.rememberMePersistentLoginEntityRepository.save(toEntity(token));
    }

    @Override
    public void updateToken(@NonNull String series, @NonNull String tokenValue, @NonNull Date lastUsed) {
        this.rememberMePersistentLoginEntityRepository.findById(series)
                                                      .ifPresent(entity -> {
                                                          entity.setToken(tokenValue);
                                                          entity.setTimestamp(lastUsed.toInstant());
                                                          this.rememberMePersistentLoginEntityRepository.save(entity);
                                                      });
    }

    @Override
    public @Nullable PersistentRememberMeToken getTokenForSeries(@NonNull String seriesId) {
        return fromEntity(this.rememberMePersistentLoginEntityRepository.findById(seriesId).orElse(null));
    }

    @Override
    public void removeUserTokens(@NonNull String username) {
        this.rememberMePersistentLoginEntityRepository.deleteByUsername(username);
    }

    private static PersistentRememberMeToken fromEntity(RememberMePersistentLoginEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PersistentRememberMeToken(
                entity.getUsername(),
                entity.getSeries(),
                entity.getToken(),
                Date.from(entity.getTimestamp()));
    }

    private static RememberMePersistentLoginEntity toEntity(PersistentRememberMeToken token) {
        if (token == null) {
            return null;
        }
        var entity = new RememberMePersistentLoginEntity();
        entity.setUsername(token.getUsername());
        entity.setSeries(token.getSeries());
        entity.setToken(token.getTokenValue());
        entity.setTimestamp(token.getDate().toInstant());
        return entity;
    }
}
