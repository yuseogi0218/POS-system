package com.yuseogi.tradeservice.infrastructure.messagequeue.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.request.DecreaseProductStockRequestMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TradeKafkaProducer {

    private static final String DECREASE_PRODUCT_STOCK_TOPIC = "decrease-product-stock";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void produceDecreaseProductStockMessage(DecreaseProductStockRequestMessage request) {
        try {
            kafkaTemplate.send(DECREASE_PRODUCT_STOCK_TOPIC, objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
}
