package com.yuseogi.storeservice.dto.response;

import java.util.List;

public record GetSettlementResponseDto(
    Integer revenue,
    Integer fee,
    Integer operatingProfit
) { }
