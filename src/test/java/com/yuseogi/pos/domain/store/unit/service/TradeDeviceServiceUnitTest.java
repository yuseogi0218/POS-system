package com.yuseogi.pos.domain.store.unit.service;

import com.yuseogi.pos.common.ServiceUnitTest;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.type.PosGrade;
import com.yuseogi.pos.domain.store.repository.TradeDeviceRepository;
import com.yuseogi.pos.domain.store.service.implementation.TradeDeviceServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.Mockito.*;

public class TradeDeviceServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private TradeDeviceServiceImpl tradeDeviceService;

    @Mock
    private TradeDeviceRepository tradeDeviceRepository;

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
