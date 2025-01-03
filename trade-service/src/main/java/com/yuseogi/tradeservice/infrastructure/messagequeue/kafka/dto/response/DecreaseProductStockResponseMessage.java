package com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.response;

import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.Status;

public record DecreaseProductStockResponseMessage(
    Status status,
    Boolean isNewTrade,
    Long tradeId,
    Long orderId
) { }
