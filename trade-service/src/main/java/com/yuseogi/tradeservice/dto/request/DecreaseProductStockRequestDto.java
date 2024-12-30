package com.yuseogi.tradeservice.dto.request;

import lombok.Builder;

public record DecreaseProductStockRequestDto(
    Long storeId,
    Integer decreasingStock
) {
    @Builder
    public DecreaseProductStockRequestDto(Long storeId, Integer decreasingStock) {
        this.storeId = storeId;
        this.decreasingStock = decreasingStock;
    }
}
