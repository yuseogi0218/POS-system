package com.yuseogi.storeservice.service;

import com.yuseogi.storeservice.dto.request.CreateStoreRequestDto;
import com.yuseogi.storeservice.entity.StoreEntity;

public interface StoreService {
    void createStore(CreateStoreRequestDto request);

    StoreEntity getStore(Long ownerUserId);
}
