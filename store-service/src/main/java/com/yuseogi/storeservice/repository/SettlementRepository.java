package com.yuseogi.storeservice.repository;

import com.yuseogi.storeservice.dto.response.GetSettlementDetailResponseDto;
import com.yuseogi.storeservice.dto.response.GetSettlementResponseDto;

import java.time.LocalDate;

public interface SettlementRepository {
    GetSettlementResponseDto getSettlement(Long userId, String dateTerm, LocalDate startDate);

    GetSettlementDetailResponseDto.Revenue getSettlementDetailRevenue(Long userId, LocalDate startDate, LocalDate endDate);

    GetSettlementDetailResponseDto.Fee getSettlementDetailFee(Long userId, LocalDate startDate, LocalDate endDate);
}
