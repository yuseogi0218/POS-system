package com.yuseogi.batchserver.dao;

import java.time.LocalDate;

public record SettlementDao(
    Long storeId,
    String dateTerm,
    LocalDate startDate,
    Integer revenue,
    Integer fee,
    Integer operatingProfit
) { }
