package com.yuseogi.pos.domain.trade.unit.entity;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.trade.entity.TradeEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class TradeEntityUnitTest {

    @Test
    void constructor() {
        // given
        StoreEntity expectedStore = mock(StoreEntity.class);
        TradeDeviceEntity expectedTradeDevice = mock(TradeDeviceEntity.class);

        // when
        TradeEntity actualTrade = new TradeEntity(expectedStore, expectedTradeDevice);

        // then
        Assertions.assertThat(actualTrade.getStore()).isEqualTo(expectedStore);
        Assertions.assertThat(actualTrade.getTradeDevice()).isEqualTo(expectedTradeDevice);
        Assertions.assertThat(actualTrade.getTradeAmount()).isEqualTo(0);
        Assertions.assertThat(actualTrade.getIsCompleted()).isFalse();
    }

    @Test
    void increaseTradeAmount() {
        // given
        TradeEntity trade = TradeEntity.builder().build();
        Integer increasingTradeAmount = 1000;
        Integer expectedTradeAmount = trade.getTradeAmount() + increasingTradeAmount;

        // when
        trade.increaseTradeAmount(increasingTradeAmount);

        // then
        Assertions.assertThat(trade.getTradeAmount()).isEqualTo(expectedTradeAmount);
    }

    @Test
    void complete() {
        // given
        TradeEntity trade = TradeEntity.builder().build();

        // when
        trade.complete();

        // then
        Assertions.assertThat(trade.getIsCompleted()).isTrue();
    }
}
