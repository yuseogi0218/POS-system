package com.yuseogi.tradeservice.infrastructure.messagequeue.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.tradeservice.dto.request.RollbackCreateOrderRequest;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.Status;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.response.DecreaseProductStockResponseMessage;
import com.yuseogi.tradeservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionKafkaConsumer {

    private final ObjectMapper objectMapper;

    private final OrderService orderService;

    @KafkaListener(topics = "decrease-product-stock-response", groupId = "decrease-product-stock-response-group")
    public void consumeDecreaseProductStockResponseMessage(String responseMessage) {
        try {
            DecreaseProductStockResponseMessage response = objectMapper.readValue(responseMessage, DecreaseProductStockResponseMessage.class);

            if (Status.NOT_REWARDED.equals(response.status())) {
                orderService.rollBackCreateOrder(RollbackCreateOrderRequest.from(response));
            }

        } catch (JsonProcessingException e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
}
