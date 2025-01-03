package com.yuseogi.storeservice.infrastructure.messagequeue.kafka.dto.response;

import com.yuseogi.storeservice.infrastructure.messagequeue.kafka.dto.Status;
import com.yuseogi.storeservice.infrastructure.messagequeue.kafka.dto.request.DecreaseProductStockRequestMessage;

public record DecreaseProductStockResponseMessage(
    Status status,
    Boolean isNewTrade,
    Long tradeId,
    Long orderId
) {
    public static DecreaseProductStockResponseMessage from(Status status, DecreaseProductStockRequestMessage requestMessage) {
        return new DecreaseProductStockResponseMessage(status, requestMessage.isNewTrade(), requestMessage.tradeId(), requestMessage.orderId());
    }
}
