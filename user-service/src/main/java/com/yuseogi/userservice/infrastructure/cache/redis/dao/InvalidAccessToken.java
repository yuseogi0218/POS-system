package com.yuseogi.userservice.infrastructure.cache.redis.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "invalidAccessToken")
public class InvalidAccessToken {

    @Id
    private Long id;

    @Indexed
    private String accessToken;

    @TimeToLive
    private Long expireIn;
}
