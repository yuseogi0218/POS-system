package com.yuseogi.pos.gateway.security.dto;

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
