package com.yuseogi.tradeservice.service;

import com.yuseogi.tradeservice.dto.request.PayWithCardRequestDto;
import com.yuseogi.tradeservice.dto.response.GetTradeIsNotCompletedResponseDto;
import com.yuseogi.tradeservice.entity.TradeEntity;

import java.util.Optional;

public interface TradeService {

    Optional<TradeEntity> getTradeIsNotCompleted(Long tradeDeviceId);

    void checkExistTradeIsNotCompleted(Long tradeDeviceId);

    GetTradeIsNotCompletedResponseDto getTradeInfoIsNotCompleted(Long tradeDeviceId);

    TradeEntity createTrade(Long tradeDeviceId);

    void payWithCash(Long tradeDeviceId);

    void payWithCard(Long tradeDeviceId, PayWithCardRequestDto request);
}
