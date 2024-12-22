package com.yuseogi.pos.common.client.dto.response;

import com.yuseogi.pos.domain.user.entity.UserEntity;
import lombok.Builder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;

@Builder
public record KakaoAccountResponseDto (
    Long id,
    LocalDateTime connected_at,
    KakaoAccount kakao_account
) {
    @Builder
    public record KakaoAccount(
        Boolean name_needs_agreement,
        String name,
        Boolean has_email,
        Boolean email_needs_agreement,
        Boolean is_email_valid,
        Boolean is_email_verified,
        String email,
        Boolean has_phone_number,
        Boolean phone_number_needs_agreement,
        String phone_number
    ) { }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(kakao_account.email, null);
    }

    public UserEntity toUserEntity() {
        return UserEntity.builder()
            .email(this.kakao_account.email)
            .name("이유석")
            .phone("01012345678")
//            .name(this.kakao_account.name)
//            .phone(this.kakao_account.phone_number)
            .build();
    }
}
