package com.yuseogi.pos.domain.user.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record SignUpKakaoRequestDto (
    @NotEmpty(message = "상점 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,20}$", message = "상점 이름의 최대 글자수는 20자 입니다.")
    String storeName,

    @NotEmpty(message = "POS 시스템 사용 등급은 필수 입력값입니다.")
    String posGrade
) { }
