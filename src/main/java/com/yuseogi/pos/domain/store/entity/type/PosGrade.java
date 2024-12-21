package com.yuseogi.pos.domain.store.entity.type;

import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.common.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PosGrade {
    BRONZE(15000, 30),
    SILVER(20000, 50),
    GOLD(30000, 100);

    private final Integer monthlyFee;

    private final Integer tradeDeviceCount;

    public static PosGrade ofRequest(String request) {
        try {
            return PosGrade.valueOf(request);
        } catch (IllegalArgumentException e) {
            throw new CustomException(CommonErrorCode.INVALID_PARAMETER, "posGrade 파라미터가 올바르지 않습니다.");
        }
    }
}