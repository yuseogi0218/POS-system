package com.yuseogi.storeservice.unit.service;

import com.yuseogi.storeservice.dto.response.GetSettlementDetailResponseDto;
import com.yuseogi.storeservice.dto.response.GetSettlementResponseDto;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.repository.SettlementRepository;
import com.yuseogi.storeservice.service.StoreService;
import com.yuseogi.storeservice.service.implementation.SettlementServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class SettlementServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private SettlementServiceImpl settlementService;

    @Mock
    private SettlementRepository settlementRepository;

    @Mock
    private StoreService storeService;

    /**
     * 상점 정산 데이터 조회 성공
     */
    @Test
    void getSettlement_성공() {
        // given
        Long userId = 1L;
        String dateTerm = "dateTerm";
        LocalDate startDate = LocalDate.of(2024, 12, 1);

        GetSettlementResponseDto expectedResponse = mock(GetSettlementResponseDto.class);

        // stub
        when(settlementRepository.getSettlement(userId, dateTerm, startDate)).thenReturn(expectedResponse);

        // when
        GetSettlementResponseDto actualResponse = settlementService.getSettlement(userId, dateTerm, startDate);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 상점 정산 세부 데이터 조회 성공
     * - 조회 기간 : DAY
     */
    @Test
    void getSettlementDetail_성공_dateTerm_DAY() {
        // given
        Long userId = 1L;
        String dateTerm = "DAY";
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = startDate.plusDays(1);

        GetSettlementDetailResponseDto.Revenue revenue = mock(GetSettlementDetailResponseDto.Revenue.class);
        Integer revenueAmount = 10000;
        when(revenue.amount()).thenReturn(revenueAmount);

        GetSettlementDetailResponseDto.Fee fee = mock(GetSettlementDetailResponseDto.Fee.class);
        Integer feeAmount = 1000;
        when(fee.amount()).thenReturn(feeAmount);

        GetSettlementDetailResponseDto expectedResponse = GetSettlementDetailResponseDto.builder()
            .revenue(revenue)
            .fee(fee)
            .build();

        // stub
        when(settlementRepository.getSettlementDetailRevenue(userId, startDate, endDate)).thenReturn(revenue);
        when(settlementRepository.getSettlementDetailFee(userId, startDate, endDate)).thenReturn(fee);

        // when
        GetSettlementDetailResponseDto actualResponse = settlementService.getSettlementDetail(userId, dateTerm, startDate);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 상점 정산 세부 데이터 조회 성공
     * - 조회 기간 : MONTH
     */
    @Test
    void getSettlementDetail_성공_dateTerm_MONTH() {
        // given
        Long userId = 1L;
        String dateTerm = "MONTH";
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        StoreEntity store = mock(StoreEntity.class);
        Integer settlementDate = 1;
        when(store.getSettlementDate()).thenReturn(settlementDate);

        LocalDate endDate = startDate.plusMonths(1).plusDays(1);

        GetSettlementDetailResponseDto.Revenue revenue = mock(GetSettlementDetailResponseDto.Revenue.class);
        Integer revenueAmount = 10000;
        when(revenue.amount()).thenReturn(revenueAmount);

        GetSettlementDetailResponseDto.Fee fee = mock(GetSettlementDetailResponseDto.Fee.class);
        Integer feeAmount = 1000;
        when(fee.amount()).thenReturn(feeAmount);

        GetSettlementDetailResponseDto expectedResponse = GetSettlementDetailResponseDto.builder()
            .revenue(revenue)
            .fee(fee)
            .build();

        // stub
        when(storeService.getStoreByOwnerUser(userId)).thenReturn(store);
        when(settlementRepository.getSettlementDetailRevenue(userId, startDate, endDate)).thenReturn(revenue);
        when(settlementRepository.getSettlementDetailFee(userId, startDate, endDate)).thenReturn(fee);

        // when
        GetSettlementDetailResponseDto actualResponse = settlementService.getSettlementDetail(userId, dateTerm, startDate);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 상점 정산 세부 데이터 조회 실패
     * - 조회 기간 : MONTH
     * - 실패 사유 : 상점 정산일과 요청 일자가 맞지않음
     */
    @Test
    void getSettlementDetail_실패_settlementDate() {
        // given
        Long userId = 1L;
        String dateTerm = "MONTH";
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        StoreEntity store = mock(StoreEntity.class);
        Integer settlementDate = 5;
        when(store.getSettlementDate()).thenReturn(settlementDate);

        // stub
        when(storeService.getStoreByOwnerUser(userId)).thenReturn(store);

        // when
        GetSettlementDetailResponseDto actualResponse = settlementService.getSettlementDetail(userId, dateTerm, startDate);

        // then
        Assertions.assertThat(actualResponse).isNull();
    }
}
