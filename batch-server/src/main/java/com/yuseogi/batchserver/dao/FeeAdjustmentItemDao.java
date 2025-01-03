package com.yuseogi.batchserver.dao;

public record FeeAdjustmentItemDao (
    Long feeAdjustmentId,
    String serviceProviderName,
    Integer feeAmount
){ }
