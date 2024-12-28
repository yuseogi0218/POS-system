package com.yuseogi.tradeservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record GetTradeIsNotCompletedResponseDto(
    Long id,
    Integer amount,
    List<GetOrderResponseDto> orderList,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt
) {
    public record GetOrderResponseDto (
        Long id,
        Integer amount,
        List<GetOrderDetailResponseDto> orderDetailList,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
    ) {
        public record GetOrderDetailResponseDto (
            Long id,
            String productName,
            String productCategory,
            Integer productPrice,
            Integer count,
            Integer totalAmount
        ) { }
    }
}
