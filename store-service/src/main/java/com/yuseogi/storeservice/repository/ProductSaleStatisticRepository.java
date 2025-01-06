package com.yuseogi.storeservice.repository;

import com.yuseogi.storeservice.dto.response.GetProductSaleStatisticResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ProductSaleStatisticRepository {

    List<GetProductSaleStatisticResponseDto.Product> findBy(Long userId, String category, String dateTerm, LocalDate startDate, String criteria);

}
