package com.yuseogi.batchserver.dao;

public record RevenueAdjustmentDao(
    Long adjustmentId,
    String productCategory,
    Integer categoryRevenueAmount
) { }
