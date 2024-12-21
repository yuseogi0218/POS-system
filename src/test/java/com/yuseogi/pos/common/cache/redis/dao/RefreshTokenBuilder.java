package com.yuseogi.pos.common.cache.redis.dao;

import com.yuseogi.pos.common.security.ExpireTime;
import org.assertj.core.api.Assertions;

import java.util.List;

public class RefreshTokenBuilder {
    public static RefreshToken build() {
        return RefreshToken.builder()
            .id("user@domain.com")
            .ip("127.0.0.1")
            .authorityList(List.of("ROLE_USER"))
            .refreshToken("refresh-token")
            .expireIn(ExpireTime.REFRESH_TOKEN.getSecond())
            .build();
    }

    public static void assertRefreshToken(RefreshToken actualRefreshToken, RefreshToken expectedRefreshToken) {
        Assertions.assertThat(actualRefreshToken.getId()).isEqualTo(expectedRefreshToken.getId());
        Assertions.assertThat(actualRefreshToken.getIp()).isEqualTo(expectedRefreshToken.getIp());
        Assertions.assertThat(actualRefreshToken.getAuthorityList()).isEqualTo(expectedRefreshToken.getAuthorityList());
        Assertions.assertThat(actualRefreshToken.getRefreshToken()).isEqualTo(expectedRefreshToken.getRefreshToken());
        Assertions.assertThat(actualRefreshToken.getExpireIn()).isEqualTo(expectedRefreshToken.getExpireIn());
    }
}
