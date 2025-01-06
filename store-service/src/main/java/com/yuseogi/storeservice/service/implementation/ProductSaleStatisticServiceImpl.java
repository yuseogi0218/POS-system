package com.yuseogi.storeservice.service.implementation;

import com.yuseogi.storeservice.dto.response.GetProductSaleStatisticResponseDto;
import com.yuseogi.storeservice.repository.ProductSaleStatisticRepository;
import com.yuseogi.storeservice.service.ProductSaleStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ProductSaleStatisticServiceImpl implements ProductSaleStatisticService {

    private final ProductSaleStatisticRepository productSaleStatisticRepository;

    @Override
    public GetProductSaleStatisticResponseDto getProductSaleStatistic(Long userId, String category, String dateTerm, LocalDate startDate, String criteria) {
        if ("COUNT".equals(criteria)) {
            return GetProductSaleStatisticResponseDto.builder()
                .productList(productSaleStatisticRepository.findBy(userId, category, dateTerm, startDate, "sale_count"))
                .build();
        } else {
            return GetProductSaleStatisticResponseDto.builder()
                .productList(productSaleStatisticRepository.findBy(userId, category, dateTerm, startDate, "sale_amount"))
                .build();
        }
    }
}
