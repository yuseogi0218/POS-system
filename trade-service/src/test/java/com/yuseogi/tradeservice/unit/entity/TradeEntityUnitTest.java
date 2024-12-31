package com.yuseogi.tradeservice.unit.entity;

import com.yuseogi.tradeservice.entity.TradeEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TradeEntityUnitTest {

    @Test
    void constructor() {
        // given
        Long expectedStoreId = 1L;
        Long expectedTradeDeviceId = 1L;

        // when
        TradeEntity actualTrade = new TradeEntity(expectedStoreId, expectedTradeDeviceId);

        // then
        Assertions.assertThat(actualTrade.getStoreId()).isEqualTo(expectedStoreId);
        Assertions.assertThat(actualTrade.getTradeDeviceId()).isEqualTo(expectedTradeDeviceId);
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
