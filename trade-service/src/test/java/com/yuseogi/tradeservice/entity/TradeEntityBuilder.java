package com.yuseogi.tradeservice.entity;

import org.assertj.core.api.Assertions;

public class TradeEntityBuilder {

    public static TradeEntity build() {
        TradeEntity trade = new TradeEntity(1L, 1L);
        trade.increaseTradeAmount(3600);

        return trade;
    }

    public static void assertTrade(TradeEntity actualTrade, TradeEntity expectedTrade) {
        Assertions.assertThat(actualTrade.getStoreId()).isEqualTo(expectedTrade.getStoreId());
        Assertions.assertThat(actualTrade.getTradeDeviceId()).isEqualTo(expectedTrade.getTradeDeviceId());
        Assertions.assertThat(actualTrade.getTradeAmount()).isEqualTo(expectedTrade.getTradeAmount());
        Assertions.assertThat(actualTrade.getIsCompleted()).isEqualTo(expectedTrade.getIsCompleted());
    }
}
