package com.yuseogi.batchserver.dao;

public record RevenueAdjustmentItemDao(
    Long revenueAdjustmentId,
    String productName,
    Integer productPrice,
    Integer saleCount,
    Integer saleAmount
) { }
