package com.yuseogi.pos.domain.trade.service.implementation;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.service.StoreService;
import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import com.yuseogi.pos.domain.trade.entity.TradeEntity;
import com.yuseogi.pos.domain.trade.repository.TradeRepository;
import com.yuseogi.pos.domain.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;

    private final TradeDeviceService tradeDeviceService;

    @Override
    public Optional<TradeEntity> getTradeIsNotCompleted(Long tradeDeviceId) {
        return tradeRepository.findFirstByTradeDeviceIdAndIsNotCompleted(tradeDeviceId);
    }

    @Override
    public TradeEntity createTrade(Long tradeDeviceId) {
        TradeDeviceEntity tradeDevice = tradeDeviceService.getTradeDevice(tradeDeviceId);

        TradeEntity trade = TradeEntity.builder()
            .store(tradeDevice.getStore())
            .tradeDevice(tradeDevice)
            .build();

        return tradeRepository.save(trade);
    }
}
