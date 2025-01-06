package com.yuseogi.storeservice.repository;

import com.yuseogi.storeservice.dto.response.GetSettlementResponseDto;

import java.time.LocalDate;

public interface SettlementRepository {
    GetSettlementResponseDto getSettlement(Long userId, String dateTerm, LocalDate startDate);
}
