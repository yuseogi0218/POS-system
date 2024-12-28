package com.yuseogi.pos.gateway.cache.redis.dao;

import com.yuseogi.pos.gateway.security.ExpireTime;
import org.assertj.core.api.Assertions;

public class InvalidAccessTokenBuilder {
    public static InvalidAccessToken build() {
        return InvalidAccessToken.builder()
            .id("user@domain.com")
            .accessToken("access-token")
            .expireIn(ExpireTime.ACCESS_TOKEN.getSecond())
            .build();
    }

    public static void assertInvalidAccessToken(InvalidAccessToken actualInvalidAccessToken, InvalidAccessToken expectedInvalidAccessToken) {
        Assertions.assertThat(actualInvalidAccessToken.getId()).isEqualTo(expectedInvalidAccessToken.getId());
        Assertions.assertThat(actualInvalidAccessToken.getAccessToken()).isEqualTo(expectedInvalidAccessToken.getAccessToken());
        Assertions.assertThat(actualInvalidAccessToken.getExpireIn()).isEqualTo(expectedInvalidAccessToken.getExpireIn());
    }
}
