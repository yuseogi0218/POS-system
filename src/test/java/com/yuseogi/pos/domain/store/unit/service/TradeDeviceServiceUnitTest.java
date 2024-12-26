package com.yuseogi.pos.domain.store.unit.service;

import com.yuseogi.pos.common.ServiceUnitTest;
import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.entity.type.PosGrade;
import com.yuseogi.pos.domain.store.exception.StoreErrorCode;
import com.yuseogi.pos.domain.store.repository.TradeDeviceRepository;
import com.yuseogi.pos.domain.store.service.implementation.TradeDeviceServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.mockito.Mockito.*;

public class TradeDeviceServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private TradeDeviceServiceImpl tradeDeviceService;

    @Mock
    private TradeDeviceRepository tradeDeviceRepository;

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 주문용 태블릿 기기 조회 성공
     */
    @Test
    void getTradeDevice_성공() {
        // given
        Long tradeDeviceId = 1L;
        TradeDeviceEntity expectedTradeDevice = mock(TradeDeviceEntity.class);

        // stub
        when(tradeDeviceRepository.findFirstById(tradeDeviceId)).thenReturn(Optional.of(expectedTradeDevice));

        // when
        TradeDeviceEntity actualTradeDevice = tradeDeviceService.getTradeDevice(tradeDeviceId);

        // then
        Assertions.assertThat(actualTradeDevice).isEqualTo(expectedTradeDevice);
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 주문용 태블릿 기기 조회 실패
     * - 실패 사유 : 존재하지 않는 주문용 태블릿 기기
     */
    @Test
    void getTradeDevice_실패_NOT_FOUND_TRADE_DEVICE() {
        // given
        Long unknownTradeDeviceId = 0L;

        // stub
        when(tradeDeviceRepository.findFirstById(unknownTradeDeviceId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> tradeDeviceService.getTradeDevice(unknownTradeDeviceId))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.NOT_FOUND_TRADE_DEVICE.getMessage());
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 주문용 태블릿 기기 존재 유무 확인 성공
     */
    @Test
    void checkExistTradeDevice_성공() {
        // given
        Long tradeDeviceId = 1L;

        // stub
        when(tradeDeviceRepository.existsById(tradeDeviceId)).thenReturn(Boolean.TRUE);

        // when
        tradeDeviceService.checkExistTradeDevice(tradeDeviceId);

        // then
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 주문용 태블릿 기기 존재 유무 확인 실패
     * - 실패 사유 : 존재하지 않는 주문용 태블릿 기기
     */
    @Test
    void checkExistTradeDevice_실패() {
        // given
        Long unknownTradeDeviceId = 0L;

        // when & then
        Assertions.assertThatThrownBy(() -> tradeDeviceService.checkExistTradeDevice(unknownTradeDeviceId))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.NOT_FOUND_TRADE_DEVICE.getMessage());
        verify(tradeDeviceRepository, times(1)).existsById(unknownTradeDeviceId);
    }

    /**
     * 상점 기반으로 주문용 태블릿 기기 생성 성공
     */
    @Test
    void createTradeDevice_성공() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        PosGrade posGrade = PosGrade.BRONZE;

        // stub
        when(store.getPosGrade()).thenReturn(posGrade);

        // when
        tradeDeviceService.createTradeDevice(store);

        // then
        verify(tradeDeviceRepository, times(1)).saveAll(
            argThat(tradeDeviceList -> tradeDeviceList.size() == posGrade.getTradeDeviceCount())
        );
    }

    /**
     * 상점 기준으로 주문용 태블릿 기기 목록 조회 성공
     */
    @Test
    void getTradeDeviceList_성공() {
        // given
        Long storeId = 1L;
        StoreEntity store = mock(StoreEntity.class);
        List<Long> exepctedTradeDeviceIdList = LongStream.range(1L, 11L).boxed().toList();

        // stub
        when(store.getId()).thenReturn(storeId);
        when(tradeDeviceRepository.findAllIdByStoreId(storeId)).thenReturn(exepctedTradeDeviceIdList);

        // when
        List<Long> actualTradeDeviceIdList = tradeDeviceService.getTradeDeviceList(store);

        // then
        Assertions.assertThat(actualTradeDeviceIdList).isEqualTo(exepctedTradeDeviceIdList);
    }
}
