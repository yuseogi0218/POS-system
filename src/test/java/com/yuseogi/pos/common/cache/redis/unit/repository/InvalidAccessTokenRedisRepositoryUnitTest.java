package com.yuseogi.pos.common.cache.redis.unit.repository;

import com.yuseogi.pos.common.cache.redis.RedisRepositoryUnitTest;
import com.yuseogi.pos.common.cache.redis.dao.InvalidAccessToken;
import com.yuseogi.pos.common.cache.redis.dao.InvalidAccessTokenBuilder;
import com.yuseogi.pos.common.cache.redis.repository.InvalidAccessTokenRedisRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.Optional;

@EnableRedisRepositories(basePackageClasses = InvalidAccessTokenRedisRepository.class)
public class InvalidAccessTokenRedisRepositoryUnitTest extends RedisRepositoryUnitTest {

    @Autowired
    InvalidAccessTokenRedisRepository invalidAccessTokenRedisRepository;

    @Test
    void findByAccessToken_존재_O() {
        // given
        InvalidAccessToken expectedInvalidAccessToken = InvalidAccessTokenBuilder.build();
        invalidAccessTokenRedisRepository.save(expectedInvalidAccessToken);

        // when
        Optional<InvalidAccessToken> optionalInvalidAccessToken = invalidAccessTokenRedisRepository.findByAccessToken(expectedInvalidAccessToken.getAccessToken());

        // then
        Assertions.assertThat(optionalInvalidAccessToken.isPresent()).isTrue();
        optionalInvalidAccessToken.ifPresent(
            actualInvalidAccessToken -> InvalidAccessTokenBuilder.assertInvalidAccessToken(actualInvalidAccessToken, expectedInvalidAccessToken)
        );
    }

    @Test
    void findByAccessToken_존재_X() {
        // given
        String unknownAccessToken = "unknown-access-token";

        // when
        Optional<InvalidAccessToken> optionalInvalidAccessToken = invalidAccessTokenRedisRepository.findByAccessToken(unknownAccessToken);

        // then
        Assertions.assertThat(optionalInvalidAccessToken.isEmpty()).isTrue();
    }
}
