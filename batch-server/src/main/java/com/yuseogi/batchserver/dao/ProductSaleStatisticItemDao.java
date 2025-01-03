package com.yuseogi.batchserver.dao;

public record ProductSaleStatisticItemDao(
    Long statisticId,
    Integer countRanking,
    Integer amountRanking,
    String productName,
    Integer productPrice,
    Integer saleCount,
    Integer saleAmount
) { }
