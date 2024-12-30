package com.yuseogi.tradeservice.dto;


public record ProductInfoDto(
    Long id,
    Long storeId,
    String name,
    String category,
    Integer price,
    Integer stock,
    Integer baseStock
) { }
