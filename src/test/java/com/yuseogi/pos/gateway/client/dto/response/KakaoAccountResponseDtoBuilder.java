package com.yuseogi.pos.gateway.client.dto.response;

import com.yuseogi.pos.gateway.client.dto.response.KakaoAccountResponseDto.KakaoAccount;

import java.time.LocalDateTime;

public class KakaoAccountResponseDtoBuilder {
    public static KakaoAccountResponseDto build() {
        KakaoAccount kakaoAccount = KakaoAccount.builder()
            .name_needs_agreement(Boolean.TRUE)
            .name("username")
            .has_email(Boolean.TRUE)
            .email_needs_agreement(Boolean.TRUE)
            .email("user@domain.com")
            .has_phone_number(Boolean.TRUE)
            .phone_number_needs_agreement(Boolean.TRUE)
            .phone_number("01012345678")
            .build();

        return KakaoAccountResponseDto.builder()
            .id(1L)
            .connected_at(LocalDateTime.now())
            .kakao_account(kakaoAccount)
            .build();
    }

    public static KakaoAccountResponseDto unknownBuild() {
        KakaoAccount kakaoAccount = KakaoAccount.builder()
            .name_needs_agreement(Boolean.TRUE)
            .name("username")
            .has_email(Boolean.TRUE)
            .email_needs_agreement(Boolean.TRUE)
            .email("unknwon-user@domain.com")
            .has_phone_number(Boolean.TRUE)
            .phone_number_needs_agreement(Boolean.TRUE)
            .phone_number("01012345678")
            .build();

        return KakaoAccountResponseDto.builder()
            .id(1L)
            .connected_at(LocalDateTime.now())
            .kakao_account(kakaoAccount)
            .build();
    }
}
