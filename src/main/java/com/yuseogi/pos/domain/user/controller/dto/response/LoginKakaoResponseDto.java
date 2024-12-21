package com.yuseogi.pos.domain.user.controller.dto.response;

import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record LoginKakaoResponseDto (
    List<String> authorityList,
    String grantType,
    String accessToken,
    Long accessTokenExpireIn,
    String refreshToken,
    Long refreshTokenExpireIn
) {

    public static LoginKakaoResponseDto fromTokenInfoResponse(TokenInfoResponseDto tokenInfoResponseDto) {
        return LoginKakaoResponseDto.builder()
                .authorityList(tokenInfoResponseDto.authorityList())
                .grantType(tokenInfoResponseDto.grantType())
                .accessToken(tokenInfoResponseDto.accessToken())
                .accessTokenExpireIn(tokenInfoResponseDto.accessTokenExpireIn())
                .refreshToken(tokenInfoResponseDto.refreshToken())
                .refreshTokenExpireIn(tokenInfoResponseDto.refreshTokenExpireIn())
                .build();
    }
}
