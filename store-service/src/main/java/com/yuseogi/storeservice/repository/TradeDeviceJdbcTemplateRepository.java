package com.yuseogi.storeservice.repository;


import com.yuseogi.storeservice.entity.TradeDeviceEntity;

import java.util.List;

public interface TradeDeviceJdbcTemplateRepository {
    void saveAll(List<TradeDeviceEntity> tradeDeviceList);
}
