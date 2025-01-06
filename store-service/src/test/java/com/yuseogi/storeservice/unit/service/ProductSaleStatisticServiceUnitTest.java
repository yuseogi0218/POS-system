package com.yuseogi.storeservice.unit.service;

import com.yuseogi.storeservice.dto.response.GetProductSaleStatisticResponseDto;
import com.yuseogi.storeservice.repository.ProductSaleStatisticRepository;
import com.yuseogi.storeservice.service.implementation.ProductSaleStatisticServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

public class ProductSaleStatisticServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private ProductSaleStatisticServiceImpl productSaleStatisticService;

    @Mock
    private ProductSaleStatisticRepository productSaleStatisticRepository;

    /**
     * 상품 판매 통계 데이터 조회 성공
     * - 조회 기준 : COUNT (판매 수량)
     */
    @Test
    void getProductSaleStatistic_성공_criteria_COUNT() {
        // given
        Long userId = 1L;
        String category = "category";
        String dateTerm = "dateTerm";
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        String criteria = "COUNT";

        GetProductSaleStatisticResponseDto.Product product = mock(GetProductSaleStatisticResponseDto.Product.class);
        List<GetProductSaleStatisticResponseDto.Product> productList = List.of(product);
        GetProductSaleStatisticResponseDto expectedResponse = GetProductSaleStatisticResponseDto.builder()
            .productList(productList)
            .build();

        // stub
        when(productSaleStatisticRepository.findBy(userId, category, dateTerm, startDate, "sale_count")).thenReturn(productList);

        // when
        GetProductSaleStatisticResponseDto actualResponse = productSaleStatisticService.getProductSaleStatistic(userId, category, dateTerm, startDate, criteria);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 상품 판매 통계 데이터 조회 성공
     * - 조회 기준 : AMOUNT (판매 금액)
     */
    @Test
    void getProductSaleStatistic_성공_criteria_AMOUNT() {
        // given
        Long userId = 1L;
        String category = "category";
        String dateTerm = "dateTerm";
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        String criteria = "AMOUNT";

        GetProductSaleStatisticResponseDto.Product product = mock(GetProductSaleStatisticResponseDto.Product.class);
        List<GetProductSaleStatisticResponseDto.Product> productList = List.of(product);
        GetProductSaleStatisticResponseDto expectedResponse = GetProductSaleStatisticResponseDto.builder()
            .productList(productList)
            .build();

        // stub
        when(productSaleStatisticRepository.findBy(userId, category, dateTerm, startDate, "sale_amount")).thenReturn(productList);

        // when
        GetProductSaleStatisticResponseDto actualResponse = productSaleStatisticService.getProductSaleStatistic(userId, category, dateTerm, startDate, criteria);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
