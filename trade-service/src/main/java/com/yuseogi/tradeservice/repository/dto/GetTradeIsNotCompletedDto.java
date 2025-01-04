package com.yuseogi.tradeservice.repository.dto;


import java.sql.Timestamp;
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
    Timestamp orderCreatedAt,
    Timestamp tradeCreatedAt
) { }
