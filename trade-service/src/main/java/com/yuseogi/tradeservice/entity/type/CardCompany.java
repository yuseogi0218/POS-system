package com.yuseogi.tradeservice.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardCompany {
    K(0.4),
    H(0.6),
    S(0.8);

    private final Double feeRate;
}
