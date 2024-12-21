package com.yuseogi.pos.common.security.dto;

import com.yuseogi.pos.common.cache.redis.dao.RefreshToken;
import lombok.Builder;

import java.util.List;

@Builder
public record TokenInfoResponseDto (
    List<String> authorityList,
    String grantType,
    String accessToken,
    Long accessTokenExpireIn,
    String refreshToken,
    Long refreshTokenExpireIn
){}
