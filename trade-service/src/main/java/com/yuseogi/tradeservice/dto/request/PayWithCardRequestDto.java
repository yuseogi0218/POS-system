package com.yuseogi.tradeservice.dto.request;

import com.yuseogi.common.validation.constraints.AllowedStringValues;
import jakarta.validation.constraints.NotEmpty;

public record PayWithCardRequestDto(

    @NotEmpty(message = "카드 회사는 필수 선택값입니다.")
    @AllowedStringValues(allowedValues = {"K", "H", "S"}, message = "카드 회사는 K, H, S 중 하나 이어야 합니다.")
    String cardCompany
) { }
