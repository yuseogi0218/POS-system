package com.yuseogi.storeservice.service;

import com.yuseogi.storeservice.dto.response.GetProductSaleStatisticResponseDto;

import java.time.LocalDate;

public interface ProductSaleStatisticService {
    GetProductSaleStatisticResponseDto getProductSaleStatistic(Long userId, String category, String dateTerm, LocalDate startDate, String criteria);
}
