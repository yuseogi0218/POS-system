package com.yuseogi.storeservice.entity;

public class TradeDeviceEntityBuilder {

    public static TradeDeviceEntity build() {
        StoreEntity store = StoreEntityBuilder.build();
        return new TradeDeviceEntity(store);
    }

    public static void assertTradeDevice(TradeDeviceEntity actualTradeDevice, TradeDeviceEntity expectedTradeDevice) {
        StoreEntityBuilder.assertStore(actualTradeDevice.getStore(), expectedTradeDevice.getStore());
    }
}
