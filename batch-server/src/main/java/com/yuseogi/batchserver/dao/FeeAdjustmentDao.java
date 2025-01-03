package com.yuseogi.batchserver.dao;

public record FeeAdjustmentDao(
    Long adjustmentId,
    String feeCategory,
    Integer categoryFeeAmount
) { }
