package com.yuseogi.storeservice.unit.entity;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import com.yuseogi.storeservice.exception.StoreErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TradeDeviceEntityUnitTest {

    @Test
    void constructor() {
        // given
        StoreEntity expectedStore = mock(StoreEntity.class);

        // when
        TradeDeviceEntity actualTradeDevice = new TradeDeviceEntity(expectedStore);

        // then
        Assertions.assertThat(actualTradeDevice.getStore()).isEqualTo(expectedStore);
    }

    @Test
    void checkAuthority_성공() {
        // given
        Long storeId = 1L;
        StoreEntity store = mock(StoreEntity.class);
        TradeDeviceEntity tradeDevice = TradeDeviceEntity.builder()
            .store(store)
            .build();

        // stub
        when(store.getId()).thenReturn(storeId);

        // when
        tradeDevice.checkAuthority(storeId);

        // then
    }

    @Test
    void checkAuthority_실패_DENIED_ACCESS_TO_TRADE_DEVICE() {
        // given
        Long storeId = 1L;
        StoreEntity store = mock(StoreEntity.class);
        TradeDeviceEntity tradeDevice = TradeDeviceEntity.builder()
            .store(store)
            .build();

        Long anotherStoreId = 2L;

        // stub
        when(store.getId()).thenReturn(storeId);

        // when & then
        Assertions.assertThatThrownBy(() -> tradeDevice.checkAuthority(anotherStoreId))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.DENIED_ACCESS_TO_TRADE_DEVICE.getMessage());
    }
}
