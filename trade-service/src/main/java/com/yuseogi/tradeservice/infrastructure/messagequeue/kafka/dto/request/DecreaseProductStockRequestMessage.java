package com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.request;

import lombok.Builder;

import java.util.List;

public record DecreaseProductStockRequestMessage(
    Long storeId,
    Boolean isNewTrade,
    Long tradeId,
    Long orderId,
    List<Item> itemList
) {
    public record Item(
        Long productId,
        Integer decreasingStock
    ) {
        @Builder
        public Item(Long productId, Integer decreasingStock) {
            this.productId = productId;
            this.decreasingStock = decreasingStock;
        }
    }

    @Builder
    public DecreaseProductStockRequestMessage(Long storeId, Boolean isNewTrade, Long tradeId, Long orderId, List<Item> itemList) {
        this.storeId = storeId;
        this.isNewTrade = isNewTrade;
        this.tradeId = tradeId;
        this.orderId = orderId;
        this.itemList = itemList;
    }
}
