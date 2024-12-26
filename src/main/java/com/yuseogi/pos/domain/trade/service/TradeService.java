package com.yuseogi.pos.domain.trade.service;

import com.yuseogi.pos.domain.trade.dto.response.GetTradeIsNotCompletedResponseDto;
import com.yuseogi.pos.domain.trade.entity.TradeEntity;

import java.util.Optional;

public interface TradeService {

    Optional<TradeEntity> getTradeIsNotCompleted(Long tradeDeviceId);

    void checkExistTradeIsNotCompleted(Long tradeDeviceId);

    GetTradeIsNotCompletedResponseDto getTradeInfoIsNotCompleted(Long tradeDeviceId);

    TradeEntity createTrade(Long tradeDeviceId);
}
