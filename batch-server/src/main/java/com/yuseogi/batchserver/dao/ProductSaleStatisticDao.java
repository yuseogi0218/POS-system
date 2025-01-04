package com.yuseogi.batchserver.dao;

import java.time.LocalDate;

public record ProductSaleStatisticDao(
    Long productId,
    String dateTerm,
    LocalDate startDate,
    Integer saleCount,
    Integer saleAmount
) { }
