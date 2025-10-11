package com.example.security.server.persistence.service;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.authentication.ott.DefaultOneTimeToken;
import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.security.server.persistence.entity.OneTimeTokenEntity;
import com.example.security.server.persistence.repository.OneTimeTokenEntityRepository;

@Service
public class OneTimeTokenService implements org.springframework.security.authentication.ott.OneTimeTokenService {
    private final OneTimeTokenEntityRepository oneTimeTokenEntityRepository;

    public OneTimeTokenService(OneTimeTokenEntityRepository oneTimeTokenEntityRepository, ThreadPoolTaskScheduler taskScheduler) {
        this.oneTimeTokenEntityRepository = oneTimeTokenEntityRepository;
        taskScheduler.schedule(() -> {
            this.oneTimeTokenEntityRepository.deleteByExpiresAtBefore(Instant.now());
        }, new CronTrigger("@hourly"));
    }

    @Override
    public @NonNull OneTimeToken generate(@NonNull GenerateOneTimeTokenRequest request) {
        Assert.notNull(request, "generateOneTimeTokenRequest cannot be null");
        var token = UUID.randomUUID().toString();
        var expiresAt = Instant.now().plus(request.getExpiresIn());
        var oneTimeToken = new DefaultOneTimeToken(token, request.getUsername(), expiresAt);
        this.oneTimeTokenEntityRepository.save(toEntity(oneTimeToken));
        return oneTimeToken;
    }

    @Override
    public @Nullable OneTimeToken consume(@NonNull OneTimeTokenAuthenticationToken authenticationToken) {
        if (authenticationToken.getTokenValue() == null) {
            return null;
        }
        var entity = this.oneTimeTokenEntityRepository.findById(authenticationToken.getTokenValue());

        if (entity.isEmpty()) {
            return null;
        } else {
            var ott = fromEntity(entity.get());
            this.oneTimeTokenEntityRepository.deleteById(entity.get().getTokenValue());
            if (isExpired(ott)) {
                return null;
            }
            return ott;
        }
    }

    private boolean isExpired(OneTimeToken ott) {
        return Instant.now().isAfter(ott.getExpiresAt());
    }

    private static OneTimeToken fromEntity(OneTimeTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        return new DefaultOneTimeToken(entity.getTokenValue(), entity.getUsername(), entity.getExpiresAt());
    }

    private static OneTimeTokenEntity toEntity(OneTimeToken oneTimeToken) {
        OneTimeTokenEntity entity = new OneTimeTokenEntity();
        entity.setTokenValue(oneTimeToken.getTokenValue());
        entity.setUsername(oneTimeToken.getUsername());
        entity.setExpiresAt(oneTimeToken.getExpiresAt());
        return entity;
    }
}
