package com.yuseogi.tradeservice.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardCompany {
    K(0.004),
    H(0.006),
    S(0.008);

    private final Double feeRate;
}
