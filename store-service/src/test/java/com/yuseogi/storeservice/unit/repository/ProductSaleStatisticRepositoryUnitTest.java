package com.yuseogi.storeservice.unit.repository;

import com.yuseogi.storeservice.dto.response.GetProductSaleStatisticResponseDto;
import com.yuseogi.storeservice.repository.implementation.ProductSaleStatisticRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class ProductSaleStatisticRepositoryUnitTest extends JdbcTemplateRepositoryUnitTest {

    private ProductSaleStatisticRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        this.repository = new ProductSaleStatisticRepositoryImpl(jdbcTemplate);
    }

    @Test
    void findBy() {
        // given
        Long userId = 1L;
        String category = "SUB_MENU";
        String dateTerm = "DAY";
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        String criteria = "sale_count";

        GetProductSaleStatisticResponseDto.Product product = new GetProductSaleStatisticResponseDto.Product("상품 이름 2", 12, 6000);
        List<GetProductSaleStatisticResponseDto.Product> expectedProductList = List.of(product);

        // when
        List<GetProductSaleStatisticResponseDto.Product> actualProductList = repository.findBy(userId, category, dateTerm, startDate, criteria);

        // then
        Assertions.assertThat(actualProductList).isEqualTo(expectedProductList);
    }

}
