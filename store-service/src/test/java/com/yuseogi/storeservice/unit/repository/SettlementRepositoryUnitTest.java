package com.yuseogi.storeservice.unit.repository;

import com.yuseogi.storeservice.dto.response.GetSettlementDetailResponseDto;
import com.yuseogi.storeservice.dto.response.GetSettlementResponseDto;
import com.yuseogi.storeservice.repository.implementation.SettlementRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class SettlementRepositoryUnitTest extends JdbcTemplateRepositoryUnitTest {

    private SettlementRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        this.repository = new SettlementRepositoryImpl(jdbcTemplate);

        jdbcTemplate.update("CREATE ALIAS IF NOT EXISTS DATE FOR \"com.yuseogi.storeservice.util.H2CustomUtil.date\"");
    }

    @Test
    void getSettlement() {
        // given
        Long userId = 1L;
        String dateTerm = "DAY";
        LocalDate startDate = LocalDate.of(2024, 12, 1);

        GetSettlementResponseDto expectedSettlement = new GetSettlementResponseDto(7500, 23, 7477);

        // when
        GetSettlementResponseDto actualSettlement = repository.getSettlement(userId, dateTerm, startDate);

        // then
        Assertions.assertThat(actualSettlement).isEqualTo(expectedSettlement);
    }

    @Test
    void getSettlementDetailRevenue() {
        // given
        Long userId = 1L;
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = startDate.plusDays(1);

        GetSettlementDetailResponseDto.Revenue.ProductCategory.Product product1 = new GetSettlementDetailResponseDto.Revenue.ProductCategory.Product("상품 이름 2", "SUB_MENU", 12, 6000);
        GetSettlementDetailResponseDto.Revenue.ProductCategory productCategory_SUB_MENU = new GetSettlementDetailResponseDto.Revenue.ProductCategory("SUB_MENU", List.of(product1));

        GetSettlementDetailResponseDto.Revenue.ProductCategory.Product product2 = new GetSettlementDetailResponseDto.Revenue.ProductCategory.Product("상품 이름 4", "DRINK", 5, 1500);
        GetSettlementDetailResponseDto.Revenue.ProductCategory productCategory_DRINK = new GetSettlementDetailResponseDto.Revenue.ProductCategory("DRINK", List.of(product2));

        GetSettlementDetailResponseDto.Revenue expectedRevenue = new GetSettlementDetailResponseDto.Revenue(List.of(productCategory_DRINK, productCategory_SUB_MENU));


        // when
        GetSettlementDetailResponseDto.Revenue actualRevenue = repository.getSettlementDetailRevenue(userId, startDate, endDate);

        // then
        Assertions.assertThat(actualRevenue).isEqualTo(expectedRevenue);
    }

    @Test
    void getSettlementDetailFee() {
        // given
        Long userId = 1L;
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = startDate.plusDays(1);

        GetSettlementDetailResponseDto.Fee.CardCompany cardCompany = new GetSettlementDetailResponseDto.Fee.CardCompany("H", 23);
        GetSettlementDetailResponseDto.Fee expectedFee = new GetSettlementDetailResponseDto.Fee(List.of(cardCompany));


        // when
        GetSettlementDetailResponseDto.Fee actualFee = repository.getSettlementDetailFee(userId, startDate, endDate);

        // then
        Assertions.assertThat(actualFee).isEqualTo(expectedFee);
    }

}
