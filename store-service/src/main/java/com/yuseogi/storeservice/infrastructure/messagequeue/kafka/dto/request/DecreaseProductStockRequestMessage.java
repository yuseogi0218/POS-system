package com.yuseogi.storeservice.infrastructure.messagequeue.kafka.dto.request;

public record DecreaseProductStockRequestMessage(
    Boolean isNewTrade,
    Long tradeId,
    Long orderId,
    Long productId,
    Long storeId,
    Integer decreasingStock
) {}
