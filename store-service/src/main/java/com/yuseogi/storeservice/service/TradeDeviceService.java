package com.yuseogi.storeservice.service;


import com.yuseogi.storeservice.dto.TradeDeviceInfoDto;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;

import java.util.List;

public interface TradeDeviceService {

    TradeDeviceEntity getTradeDevice(Long tradeDeviceId);

    TradeDeviceInfoDto getTradeDeviceInfo(Long tradeDeviceId);

    void checkExistTradeDevice(Long tradeDeviceId);

    void createTradeDevice(StoreEntity store);

    List<Long> getTradeDeviceList(StoreEntity store);

}
