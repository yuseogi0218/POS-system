package com.yuseogi.tradeservice.service.implementation;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import com.yuseogi.storeservice.service.TradeDeviceService;
import com.yuseogi.tradeservice.dto.request.PayWithCardRequestDto;
import com.yuseogi.tradeservice.dto.response.GetTradeIsNotCompletedResponseDto;
import com.yuseogi.tradeservice.entity.PaymentEntity;
import com.yuseogi.tradeservice.entity.TradeEntity;
import com.yuseogi.tradeservice.entity.type.CardCompany;
import com.yuseogi.tradeservice.exception.TradeErrorCode;
import com.yuseogi.tradeservice.repository.PaymentRepository;
import com.yuseogi.tradeservice.repository.TradeRepository;
import com.yuseogi.tradeservice.repository.dto.mapper.GetTradeIsNotCompletedDtoMapper;
import com.yuseogi.tradeservice.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TradeServiceImpl implements TradeService {

    private final PaymentRepository paymentRepository;
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

    @Transactional
    @Override
    public void payWithCash(Long tradeDeviceId) {
        TradeEntity trade = getTradeIsNotCompleted(tradeDeviceId)
            .orElseThrow(() -> new CustomException(TradeErrorCode.NOT_FOUND_TRADE_IS_NOT_COMPLETED));
        trade.complete();

        PaymentEntity payment = PaymentEntity.builderAsCashPay()
            .trade(trade)
            .buildAsCashPay();

        paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public void payWithCard(Long tradeDeviceId, PayWithCardRequestDto request) {
        TradeEntity trade = getTradeIsNotCompleted(tradeDeviceId)
            .orElseThrow(() -> new CustomException(TradeErrorCode.NOT_FOUND_TRADE_IS_NOT_COMPLETED));
        trade.complete();

        PaymentEntity payment = PaymentEntity.builderAsCardPay()
            .trade(trade)
            .cardCompany(CardCompany.valueOf(request.cardCompany()))
            .buildAsCardPay();

        paymentRepository.save(payment);
    }
}
