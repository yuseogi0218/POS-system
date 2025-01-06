package com.yuseogi.storeservice.service.implementation;

import com.yuseogi.storeservice.dto.response.GetSettlementDetailResponseDto;
import com.yuseogi.storeservice.dto.response.GetSettlementResponseDto;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.repository.SettlementRepository;
import com.yuseogi.storeservice.service.SettlementService;
import com.yuseogi.storeservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class SettlementServiceImpl implements SettlementService {

    private final SettlementRepository settlementRepository;

    private final StoreService storeService;

    @Override
    public GetSettlementResponseDto getSettlement(Long userId, String dateTerm, LocalDate startDate) {
        return settlementRepository.getSettlement(userId, dateTerm, startDate);
    }

    @Override
    public GetSettlementDetailResponseDto getSettlementDetail(Long userId, String dateTerm, LocalDate startDate) {
        LocalDate endDate;

        if (dateTerm.equals("DAY")) {
            endDate = startDate.plusDays(1);
        } else {
            // dateTerm = MONTH
            StoreEntity store = storeService.getStoreByOwnerUser(userId);

            if (!store.getSettlementDate().equals(startDate.getDayOfMonth())) {
                return null;
            }

            endDate = startDate.plusMonths(1).plusDays(1);
        }

        GetSettlementDetailResponseDto.Revenue revenue = settlementRepository.getSettlementDetailRevenue(userId, startDate, endDate);
        GetSettlementDetailResponseDto.Fee fee = settlementRepository.getSettlementDetailFee(userId, startDate, endDate);

        return GetSettlementDetailResponseDto.builder()
            .revenue(revenue)
            .fee(fee)
            .build();
    }
}
