package com.yuseogi.storeservice.infrastructure.messagequeue.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.infrastructure.messagequeue.kafka.dto.response.DecreaseProductStockResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TransactionKafkaProducer {

    private static final String DECREASE_PRODUCT_STOCK_TRANSACTION_RESULT_TOPIC = "decrease-product-stock-response";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void produceDecreaseProductStockResponseMessage(DecreaseProductStockResponseMessage responseMessage) {
        try {
            String message = objectMapper.writeValueAsString(responseMessage);
            kafkaTemplate.send(DECREASE_PRODUCT_STOCK_TRANSACTION_RESULT_TOPIC, message);
        } catch (JsonProcessingException e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
