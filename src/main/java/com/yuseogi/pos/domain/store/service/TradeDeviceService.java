package com.yuseogi.pos.domain.store.service;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;

import java.util.List;

public interface TradeDeviceService {

    TradeDeviceEntity getTradeDevice(Long tradeDeviceId);

    void checkExistTradeDevice(Long tradeDeviceId);

    void createTradeDevice(StoreEntity store);

    List<Long> getTradeDeviceList(StoreEntity store);

}
