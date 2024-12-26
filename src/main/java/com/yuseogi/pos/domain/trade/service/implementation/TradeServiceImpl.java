package com.yuseogi.pos.domain.trade.service.implementation;

import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import com.yuseogi.pos.domain.trade.dto.response.GetTradeIsNotCompletedResponseDto;
import com.yuseogi.pos.domain.trade.entity.TradeEntity;
import com.yuseogi.pos.domain.trade.exception.TradeErrorCode;
import com.yuseogi.pos.domain.trade.repository.TradeRepository;
import com.yuseogi.pos.domain.trade.repository.dto.mapper.GetTradeIsNotCompletedDtoMapper;
import com.yuseogi.pos.domain.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void checkExistTradeIsNotCompleted(Long tradeDeviceId) {
        if (!tradeRepository.existsByTradeDeviceIdAndIsNotCompleted(tradeDeviceId)) {
            throw new CustomException(TradeErrorCode.NOT_FOUND_TRADE_IS_NOT_COMPLETED);
        }
    }

    @Transactional
    @Override
    public GetTradeIsNotCompletedResponseDto getTradeInfoIsNotCompleted(Long tradeDeviceId) {
        checkExistTradeIsNotCompleted(tradeDeviceId);

        return GetTradeIsNotCompletedDtoMapper.mapToResponseDto(tradeRepository.findByTradeDeviceAndIsNotCompleted(tradeDeviceId));
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
