package com.yuseogi.storeservice.dto.request;

public record DecreaseProductStockRequestDto(
    Long storeId,
    Integer decreasingStock
) { }
