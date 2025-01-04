package com.yuseogi.userservice.dto.request;

public class SignUpKakaoRequestDtoBuilder {
    public static SignUpKakaoRequestDto build() {
        return new SignUpKakaoRequestDto("상점 이름", "BRONZE", 1);
    }

    public static SignUpKakaoRequestDto nullStoreNameBuild() {
        return new SignUpKakaoRequestDto(null, "BRONZE", 1);
    }

    public static SignUpKakaoRequestDto emptyStoreNameBuild() {
        return new SignUpKakaoRequestDto("", "BRONZE", 1);
    }

    public static SignUpKakaoRequestDto invalidStoreNameBuild() {
        return new SignUpKakaoRequestDto("상점 이름".repeat(5), "BRONZE", 1);
    }

    public static SignUpKakaoRequestDto nullPosGradeBuild() {
        return new SignUpKakaoRequestDto("상점 이름", null, 1);
    }

    public static SignUpKakaoRequestDto emptyPosGradeBuild() {
        return new SignUpKakaoRequestDto("상점 이름", "", 1);
    }

    public static SignUpKakaoRequestDto invalidPosGradeBuild() {
        return new SignUpKakaoRequestDto("상점 이름", "INVALID_POS_GRADE", 1);
    }

    public static SignUpKakaoRequestDto nullSettlementDateBuild() {
        return new SignUpKakaoRequestDto("상점 이름", "BRONZE", null);
    }

    public static SignUpKakaoRequestDto invalidSettlementDateBuild() {
        return new SignUpKakaoRequestDto("상점 이름", "BRONZE", 2);
    }
}
