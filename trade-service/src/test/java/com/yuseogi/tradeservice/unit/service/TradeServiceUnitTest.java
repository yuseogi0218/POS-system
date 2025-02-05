package com.yuseogi.tradeservice.unit.service;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.tradeservice.dto.TradeDeviceInfoDto;
import com.yuseogi.tradeservice.dto.request.PayWithCardRequestDto;
import com.yuseogi.tradeservice.dto.response.GetTradeIsNotCompletedResponseDto;
import com.yuseogi.tradeservice.entity.PaymentEntity;
import com.yuseogi.tradeservice.entity.TradeEntity;
import com.yuseogi.tradeservice.exception.TradeErrorCode;
import com.yuseogi.tradeservice.infrastructure.client.StoreServiceClient;
import com.yuseogi.tradeservice.repository.PaymentRepository;
import com.yuseogi.tradeservice.repository.TradeRepository;
import com.yuseogi.tradeservice.repository.dto.GetTradeIsNotCompletedDto;
import com.yuseogi.tradeservice.service.implementation.TradeServiceImpl;
import com.yuseogi.tradeservice.dto.response.GetTradeIsNotCompletedResponseDtoBuilder;
import com.yuseogi.tradeservice.unit.repository.dto.GetTradeIsNotCompletedDtoBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class TradeServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private TradeServiceImpl tradeService;

    @Mock
    private StoreServiceClient storeServiceClient;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private TradeRepository tradeRepository;

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 완료되지 않은 거래 Entity 조회 성공 - 존재 O
     */
    @Test
    void getTradeIsNotCompleted_존재_O() {
        // given
        Long tradeDeviceId = 1L;
        TradeEntity expectedTrade = mock(TradeEntity.class);

        // stub
        when(tradeRepository.findFirstByTradeDeviceIdAndIsNotCompleted(tradeDeviceId)).thenReturn(Optional.of(expectedTrade));

        // when
        Optional<TradeEntity> optionalTrade = tradeService.getTradeIsNotCompleted(tradeDeviceId);

        // then
        Assertions.assertThat(optionalTrade.isPresent()).isTrue();
        optionalTrade.ifPresent(
            actualTrade -> Assertions.assertThat(actualTrade).isEqualTo(expectedTrade)
        );
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 완료되지 않은 거래 Entity 조회 성공 - 존재 X
     */
    @Test
    void getTradeIsNotCompleted_존재_X() {
        // given
        Long tradeDeviceId = 1L;

        // stub
        when(tradeRepository.findFirstByTradeDeviceIdAndIsNotCompleted(tradeDeviceId)).thenReturn(Optional.empty());

        // when
        Optional<TradeEntity> optionalTrade = tradeService.getTradeIsNotCompleted(tradeDeviceId);

        // then
        Assertions.assertThat(optionalTrade.isEmpty()).isTrue();
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 완료되지 않은 거래 존재 유무 확인 성공
     */
    @Test
    void checkExistTradeIsNotCompleted_성공() {
        // given
        Long tradeDeviceId = 1L;

        // stub
        when(tradeRepository.existsByTradeDeviceIdAndIsNotCompleted(tradeDeviceId)).thenReturn(Boolean.TRUE);

        // when
        tradeService.checkExistTradeIsNotCompleted(tradeDeviceId);

        // then
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 완료되지 않은 거래 존재 유무 확인 실패
     * - 완료되지 않은 거래 존재 X
     */
    @Test
    void checkExistTradeIsNotCompleted_실패_NOT_FOUND_TRADE_IS_NOT_COMPLETED() {
        // given
        Long tradeDeviceId = 1L;

        // stub
        when(tradeRepository.existsByTradeDeviceIdAndIsNotCompleted(tradeDeviceId)).thenReturn(Boolean.FALSE);

        // when & then
        Assertions.assertThatThrownBy(() -> tradeService.checkExistTradeIsNotCompleted(tradeDeviceId))
            .isInstanceOf(CustomException.class)
            .hasMessage(TradeErrorCode.NOT_FOUND_TRADE_IS_NOT_COMPLETED.getMessage());
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 완료되지 않은 거래 정보(주문 내역) 조회 성공
     */
    @Test
    void getTradeInfoIsNotCompleted() {
        // given
        Long tradeDeviceId = 1L;
        List<GetTradeIsNotCompletedDto> expectedGetTradeIsNotCompletedDtoList = GetTradeIsNotCompletedDtoBuilder.buildList();
        GetTradeIsNotCompletedResponseDto expectedGetTradeIsNotCompletedResponse = GetTradeIsNotCompletedResponseDtoBuilder.build();

        // stub
        when(tradeRepository.existsByTradeDeviceIdAndIsNotCompleted(tradeDeviceId)).thenReturn(Boolean.TRUE);
        when(tradeRepository.findByTradeDeviceAndIsNotCompleted(tradeDeviceId)).thenReturn(expectedGetTradeIsNotCompletedDtoList);

        // when
        GetTradeIsNotCompletedResponseDto actualGetTradeIsNotCompletedResponse = tradeService.getTradeInfoIsNotCompleted(tradeDeviceId);

        // then
        Assertions.assertThat(actualGetTradeIsNotCompletedResponse).isEqualTo(expectedGetTradeIsNotCompletedResponse);
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 거래 생성 성공
     */
    @Test
    void createTrade() {
        // given
        Long tradeDeviceId = 1L;
        TradeDeviceInfoDto tradeDevice = mock(TradeDeviceInfoDto.class);
        Long storeId = 1L;
        TradeEntity expectedTrade = mock(TradeEntity.class);


        // stub
        when(storeServiceClient.getTradeDevice(tradeDeviceId)).thenReturn(tradeDevice);
        when(tradeDevice.storeId()).thenReturn(storeId);
        when(tradeDevice.id()).thenReturn(tradeDeviceId);
        when(tradeRepository.save(any(TradeEntity.class))).thenReturn(expectedTrade);

        // when
        TradeEntity actualTrade = tradeService.createTrade(tradeDeviceId);

        // then
        Assertions.assertThat(actualTrade).isEqualTo(expectedTrade);
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 완료되지 않은 거래(주문 내역)에 대한 일괄 현금 결제 성공
     */
    @Test
    void payWithCash_성공() {
        // given
        Long tradeDeviceId = 1L;
        TradeEntity trade = mock(TradeEntity.class);

        // stub
        when(tradeRepository.findFirstByTradeDeviceIdAndIsNotCompleted(tradeDeviceId)).thenReturn(Optional.of(trade));

        // when
        tradeService.payWithCash(tradeDeviceId);

        // then
        verify(trade, times(1)).complete();
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 완료되지 않은 거래(주문 내역)에 대한 일괄 현금 결제 실패
     * - 실패 사유 : 완료되지 않은 거래 존재 X
     */
    @Test
    void payWithCash_실패_NOT_FOUND_TRADE_IS_NOT_COMPLETED() {
        // given
        Long tradeDeviceId = 1L;

        // stub
        when(tradeRepository.findFirstByTradeDeviceIdAndIsNotCompleted(tradeDeviceId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> tradeService.payWithCash(tradeDeviceId))
            .isInstanceOf(CustomException.class)
            .hasMessage(TradeErrorCode.NOT_FOUND_TRADE_IS_NOT_COMPLETED.getMessage());
        verify(paymentRepository, never()).save(any(PaymentEntity.class));
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 완료되지 않은 거래(주문 내역)에 대한 일괄 카드 결제 성공
     */
    @Test
    void payWithCard_성공() {
        // given
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto request = mock(PayWithCardRequestDto.class);
        TradeEntity trade = mock(TradeEntity.class);

        // stub
        when(tradeRepository.findFirstByTradeDeviceIdAndIsNotCompleted(tradeDeviceId)).thenReturn(Optional.of(trade));
        when(request.cardCompany()).thenReturn("K");

        // when
        tradeService.payWithCard(tradeDeviceId, request);

        // then
        verify(trade, times(1)).complete();
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 완료되지 않은 거래(주문 내역)에 대한 일괄 카드 결제 실패
     * - 실패 사유 : 완료되지 않은 거래 존재 X
     */
    @Test
    void payWithCard_실패_NOT_FOUND_TRADE_IS_NOT_COMPLETED() {
        // given
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto request = mock(PayWithCardRequestDto.class);

        // stub
        when(tradeRepository.findFirstByTradeDeviceIdAndIsNotCompleted(tradeDeviceId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> tradeService.payWithCard(tradeDeviceId, request))
            .isInstanceOf(CustomException.class)
            .hasMessage(TradeErrorCode.NOT_FOUND_TRADE_IS_NOT_COMPLETED.getMessage());
        verify(paymentRepository, never()).save(any(PaymentEntity.class));
    }
}
