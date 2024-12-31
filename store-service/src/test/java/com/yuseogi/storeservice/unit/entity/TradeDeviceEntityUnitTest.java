package com.yuseogi.storeservice.unit.entity;

import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

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

}
