package com.yuseogi.storeservice.entity.type;

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

}