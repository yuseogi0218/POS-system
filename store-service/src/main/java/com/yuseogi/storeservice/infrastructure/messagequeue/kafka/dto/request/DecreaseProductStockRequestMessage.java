package com.yuseogi.storeservice.infrastructure.messagequeue.kafka.dto.request;

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
    ) { }

}
