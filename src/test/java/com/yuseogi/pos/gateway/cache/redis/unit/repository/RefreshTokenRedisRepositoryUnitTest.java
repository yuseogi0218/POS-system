package com.yuseogi.pos.gateway.cache.redis.unit.repository;

import com.yuseogi.pos.gateway.cache.redis.RedisRepositoryUnitTest;
import com.yuseogi.pos.gateway.cache.redis.dao.RefreshToken;
import com.yuseogi.pos.gateway.cache.redis.dao.RefreshTokenBuilder;
import com.yuseogi.pos.gateway.cache.redis.repository.RefreshTokenRedisRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.Optional;

@EnableRedisRepositories(basePackageClasses = RefreshTokenRedisRepository.class)
public class RefreshTokenRedisRepositoryUnitTest extends RedisRepositoryUnitTest {

    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Test
    void findByAccessToken_존재_O() {
        // given
        RefreshToken expectedRefreshToken = RefreshTokenBuilder.build();
        refreshTokenRedisRepository.save(expectedRefreshToken);

        // when
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRedisRepository.findByRefreshToken(expectedRefreshToken.getRefreshToken());

        // then
        Assertions.assertThat(optionalRefreshToken.isPresent()).isTrue();
        optionalRefreshToken.ifPresent(
            actualRefreshToken -> RefreshTokenBuilder.assertRefreshToken(actualRefreshToken, expectedRefreshToken)
        );
    }

    @Test
    void findByAccessToken_존재_X() {
        // given
        String unknownRefreshToken = "unknown-refresh-token";

        // when
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRedisRepository.findByRefreshToken(unknownRefreshToken);

        // then
        Assertions.assertThat(optionalRefreshToken.isEmpty()).isTrue();
    }
}
