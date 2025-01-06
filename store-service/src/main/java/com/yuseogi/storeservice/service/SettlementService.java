package com.yuseogi.storeservice.service;

import com.yuseogi.storeservice.dto.response.GetSettlementResponseDto;

import java.time.LocalDate;

public interface SettlementService {

    GetSettlementResponseDto getSettlement(Long userId, String dateTerm, LocalDate startDate);

}
