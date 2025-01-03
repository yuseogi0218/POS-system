package com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.request;

import lombok.Builder;

public record DecreaseProductStockRequestMessage(
    Boolean isNewTrade,
    Long tradeId,
    Long orderId,
    Long productId,
    Long storeId,
    Integer decreasingStock
) {
    @Builder
    public DecreaseProductStockRequestMessage(Boolean isNewTrade, Long tradeId, Long orderId, Long productId, Long storeId, Integer decreasingStock) {
        this.isNewTrade = isNewTrade;
        this.tradeId = tradeId;
        this.orderId = orderId;
        this.productId = productId;
        this.storeId = storeId;
        this.decreasingStock = decreasingStock;
    }
}
