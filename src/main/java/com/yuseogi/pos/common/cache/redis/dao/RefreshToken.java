package com.yuseogi.pos.common.cache.redis.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh")
public class RefreshToken {

    @Id
    private String id;

    private String ip;

    private List<String> authorityList;

    @Indexed
    private String refreshToken;

    @TimeToLive
    private Long expireIn;
}
