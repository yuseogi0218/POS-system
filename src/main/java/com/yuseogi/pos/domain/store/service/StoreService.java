package com.yuseogi.pos.domain.store.service;

import com.yuseogi.pos.domain.store.dto.request.CreateStoreRequestDto;
import com.yuseogi.pos.domain.store.entity.StoreEntity;

import java.util.List;

public interface StoreService {
    void createStore(CreateStoreRequestDto request);

    StoreEntity getStore(String ownerUserEmail);

    List<Long> getTradeDeviceList(String ownerUserEmail);
}
