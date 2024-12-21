package com.yuseogi.pos.domain.user.dto.response;

import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record LoginResponseDto(
        List<String> authorityList,
        String grantType,
        String accessToken,
        Long accessTokenExpireIn,
        String refreshToken,
        Long refreshTokenExpireIn
) {

    public static LoginResponseDto fromTokenInfoResponse(TokenInfoResponseDto tokenInfoResponseDto) {
        return LoginResponseDto.builder()
                .authorityList(tokenInfoResponseDto.authorityList())
                .grantType(tokenInfoResponseDto.grantType())
                .accessToken(tokenInfoResponseDto.accessToken())
                .accessTokenExpireIn(tokenInfoResponseDto.accessTokenExpireIn())
                .refreshToken(tokenInfoResponseDto.refreshToken())
                .refreshTokenExpireIn(tokenInfoResponseDto.refreshTokenExpireIn())
                .build();
    }
}
