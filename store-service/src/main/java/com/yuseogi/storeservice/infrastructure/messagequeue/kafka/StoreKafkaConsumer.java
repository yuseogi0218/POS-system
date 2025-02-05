package com.yuseogi.storeservice.infrastructure.messagequeue.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.infrastructure.messagequeue.kafka.dto.Status;
import com.yuseogi.storeservice.infrastructure.messagequeue.kafka.dto.request.DecreaseProductStockRequestMessage;
import com.yuseogi.storeservice.infrastructure.messagequeue.kafka.dto.response.DecreaseProductStockResponseMessage;
import com.yuseogi.storeservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StoreKafkaConsumer {

    private final ObjectMapper objectMapper;

    private final TransactionKafkaProducer transactionKafkaProducer;

    private final ProductService productService;

    @KafkaListener(topics = "decrease-product-stock", groupId = "decrease-product-stock-group")
    public void consumeDecreaseProductStockMessage(String message) {
        try {
            DecreaseProductStockRequestMessage request = objectMapper.readValue(message, DecreaseProductStockRequestMessage.class);

            try {
                productService.decreaseStock(request);
                DecreaseProductStockResponseMessage successMessage = DecreaseProductStockResponseMessage.from(Status.REWARDED, request);
                transactionKafkaProducer.produceDecreaseProductStockResponseMessage(successMessage);
            } catch (CustomException e) {
                //TODO: 2025-01-04 상품 재고 부족 시, 알림을 통해 주문자 또는 상점 관리자가 알 수 있도록 하는 방법
                DecreaseProductStockResponseMessage failMessage = DecreaseProductStockResponseMessage.from(Status.NOT_REWARDED, request);
                transactionKafkaProducer.produceDecreaseProductStockResponseMessage(failMessage);
            }
        } catch (JsonProcessingException e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
