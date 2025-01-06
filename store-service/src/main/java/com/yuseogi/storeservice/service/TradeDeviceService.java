package com.yuseogi.storeservice.service;


import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;

import java.util.List;

public interface TradeDeviceService {

    TradeDeviceEntity getTradeDevice(Long tradeDeviceId);

    void checkAuthorityTradeDevice(StoreEntity store, Long tradeDeviceId);

    void createTradeDevice(StoreEntity store);

    List<Long> getTradeDeviceList(StoreEntity store);

}
