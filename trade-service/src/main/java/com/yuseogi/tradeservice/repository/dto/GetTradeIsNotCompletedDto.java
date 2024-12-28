package com.yuseogi.tradeservice.repository.dto;


import com.yuseogi.storeservice.entity.type.ProductCategory;

import java.time.LocalDateTime;

public record GetTradeIsNotCompletedDto(
    Long tradeId,
    Integer tradeAmount,
    Long orderId,
    Integer orderAmount,
    Long orderDetailId,
    String orderDetailProductName,
    String orderDetailProductCategory,
    Integer orderDetailProductPrice,
    Integer orderDetailCount,
    Integer orderDetailTotalAmount,
    LocalDateTime orderCreatedAt,
    LocalDateTime tradeCreatedAt
) {
    public GetTradeIsNotCompletedDto(Long tradeId, Integer tradeAmount, Long orderId, Integer orderAmount, Long orderDetailId, String orderDetailProductName, ProductCategory orderDetailProductCategory, Integer orderDetailProductPrice, Integer orderDetailCount, Integer orderDetailTotalAmount, LocalDateTime orderCreatedAt, LocalDateTime tradeCreatedAt) {
        this(
            tradeId,
            tradeAmount,
            orderId,
            orderAmount,
            orderDetailId,
            orderDetailProductName,
            orderDetailProductCategory.name(),
            orderDetailProductPrice,
            orderDetailCount,
            orderDetailTotalAmount,
            orderCreatedAt,
            tradeCreatedAt
        );
    }
}
