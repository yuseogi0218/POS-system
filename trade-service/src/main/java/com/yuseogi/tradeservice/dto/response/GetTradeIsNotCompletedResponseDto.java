package com.yuseogi.tradeservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record GetTradeIsNotCompletedResponseDto(
    Long id,
    Integer amount,
    List<Order> orderList,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt
) {
    public record Order(
        Long id,
        Integer amount,
        List<OrderDetail> orderDetailList,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
    ) {
        public record OrderDetail(
            Long id,
            String productName,
            String productCategory,
            Integer productPrice,
            Integer count,
            Integer totalAmount
        ) { }
    }
}
