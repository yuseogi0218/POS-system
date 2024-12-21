package com.yuseogi.pos.domain.store.repository;

import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;

import java.util.List;

public interface TradeDeviceRepository {
    void saveAll(List<TradeDeviceEntity> tradeDeviceList);
}
