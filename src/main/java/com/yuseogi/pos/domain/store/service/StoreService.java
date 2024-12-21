package com.yuseogi.pos.domain.store.service;

import com.yuseogi.pos.domain.store.dto.request.CreateStoreRequestDto;

public interface StoreService {
    void createStore(CreateStoreRequestDto request);
}
