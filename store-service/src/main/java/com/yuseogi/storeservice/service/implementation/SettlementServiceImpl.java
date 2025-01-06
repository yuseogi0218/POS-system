package com.yuseogi.storeservice.service.implementation;

import com.yuseogi.storeservice.dto.response.GetSettlementResponseDto;
import com.yuseogi.storeservice.repository.SettlementRepository;
import com.yuseogi.storeservice.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class SettlementServiceImpl implements SettlementService {

    private final SettlementRepository settlementRepository;

    @Override
    public GetSettlementResponseDto getSettlement(Long userId, String dateTerm, LocalDate startDate) {
        return settlementRepository.getSettlement(userId, dateTerm, startDate);
    }
}
