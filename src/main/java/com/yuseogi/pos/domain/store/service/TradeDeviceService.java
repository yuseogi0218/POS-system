package com.yuseogi.pos.domain.store.service;

import com.yuseogi.pos.domain.store.entity.StoreEntity;

import java.util.List;

public interface TradeDeviceService {

    void createTradeDevice(StoreEntity savedStore);

    List<Long> getTradeDeviceList(StoreEntity store);

}
