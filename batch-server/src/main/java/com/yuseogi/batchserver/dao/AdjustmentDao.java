package com.yuseogi.batchserver.dao;

import java.time.LocalDate;

public record AdjustmentDao (
   Long storeId,
   String dateTerm,
   LocalDate startDate,
   Integer totalRevenue,
   Integer totalFee,
   Integer operatingProfit
){ }
