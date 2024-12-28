package com.yuseogi.tradeservice.dto.request;

import com.yuseogi.common.validation.constraints.ValidEnum;
import com.yuseogi.tradeservice.entity.type.CardCompany;

public record PayWithCardRequestDto(

    @ValidEnum(enumClass = CardCompany.class, message = "카드 회사는 K, H, S 중 하나 이어야 합니다.")
    String cardCompany
) { }
