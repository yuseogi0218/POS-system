package com.yuseogi.pos.domain.user.dto.request;

public class SignUpKakaoRequestDtoBuilder {
    public static SignUpKakaoRequestDto build() {
        return new SignUpKakaoRequestDto("상점 이름", "BRONZE");
    }

    public static SignUpKakaoRequestDto nullStoreNameBuild() {
        return new SignUpKakaoRequestDto(null, "BRONZE");
    }

    public static SignUpKakaoRequestDto emptyStoreNameBuild() {
        return new SignUpKakaoRequestDto("", "BRONZE");
    }

    public static SignUpKakaoRequestDto invalidStoreNameBuild() {
        return new SignUpKakaoRequestDto("상점 이름".repeat(5), "BRONZE");
    }

    public static SignUpKakaoRequestDto nullPosGradeBuild() {
        return new SignUpKakaoRequestDto("상점 이름", null);
    }

    public static SignUpKakaoRequestDto emptyPosGradeBuild() {
        return new SignUpKakaoRequestDto("상점 이름", "");
    }

    public static SignUpKakaoRequestDto invalidPosGradeBuild() {
        return new SignUpKakaoRequestDto("상점 이름", "INVALID_POS_GRADE");
    }
}
