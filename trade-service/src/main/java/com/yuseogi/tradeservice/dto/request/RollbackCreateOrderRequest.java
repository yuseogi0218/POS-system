package com.yuseogi.tradeservice.dto.request;

import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.response.DecreaseProductStockResponseMessage;

public record RollbackCreateOrderRequest(
    Boolean isNewTrade,
    Long tradeId,
    Long orderId
) {
    public static RollbackCreateOrderRequest from(DecreaseProductStockResponseMessage response) {
        return new RollbackCreateOrderRequest(response.isNewTrade(), response.tradeId(), response.orderId());
    }
}
