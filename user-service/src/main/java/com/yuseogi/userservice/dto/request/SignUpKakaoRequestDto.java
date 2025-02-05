package com.yuseogi.userservice.dto.request;

import com.yuseogi.common.validation.constraints.AllowedIntegerValues;
import com.yuseogi.common.validation.constraints.AllowedStringValues;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignUpKakaoRequestDto (
    @NotEmpty(message = "상점 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,20}$", message = "상점 이름의 최대 글자수는 20자 입니다.")
    String storeName,

    @NotEmpty(message = "POS 시스템 사용 등급은 필수 선택값입니다.")
    @AllowedStringValues(allowedValues = {"BRONZE", "SILVER", "GOLD"}, message = "POS 시스템 사용 등급은 BRONZE, SILVER, GOLD 중 하나 이어야 합니다.")
    String posGrade,

    @NotNull(message = "정산일은 필수 선택값입니다.")
    @AllowedIntegerValues(allowedValues = {1, 5, 10, 15, 20, 25}, message = "정산일은 1, 5, 10, 15, 20, 25 중 하나 이어야 합니다.")
    Integer settlementDate
) { }
