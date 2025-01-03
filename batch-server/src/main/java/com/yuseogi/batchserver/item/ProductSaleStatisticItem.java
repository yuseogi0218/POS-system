package com.yuseogi.batchserver.item;

public record ProductSaleStatisticItem (
    Long statisticId,
    Integer countRanking,
    Integer amountRanking,
    String productName,
    Integer productPrice,
    Integer saleCount,
    Integer saleAmount
) { }
