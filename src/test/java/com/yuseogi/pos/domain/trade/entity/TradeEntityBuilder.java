package com.yuseogi.pos.domain.trade.entity;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.StoreEntityBuilder;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntityBuilder;
import org.assertj.core.api.Assertions;

public class TradeEntityBuilder {

    public static TradeEntity build() {
        TradeDeviceEntity tradeDevice = TradeDeviceEntityBuilder.build();
        StoreEntity store = tradeDevice.getStore();

        TradeEntity trade = new TradeEntity(store, tradeDevice);
        trade.increaseTradeAmount(3600);

        return trade;
    }

    public static void assertTrade(TradeEntity actualTrade, TradeEntity expectedTrade) {
        StoreEntityBuilder.assertStore(actualTrade.getStore(), expectedTrade.getStore());
        TradeDeviceEntityBuilder.assertTradeDevice(actualTrade.getTradeDevice(), expectedTrade.getTradeDevice());
        Assertions.assertThat(actualTrade.getTradeAmount()).isEqualTo(expectedTrade.getTradeAmount());
        Assertions.assertThat(actualTrade.getIsCompleted()).isEqualTo(expectedTrade.getIsCompleted());
    }
}
