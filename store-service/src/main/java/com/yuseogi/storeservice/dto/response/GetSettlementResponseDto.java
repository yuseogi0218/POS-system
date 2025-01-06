package com.yuseogi.storeservice.dto.response;

public record GetSettlementResponseDto(
    Integer revenue,
    Integer fee,
    Integer operatingProfit
) { }
