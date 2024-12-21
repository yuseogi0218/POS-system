package com.yuseogi.pos.domain.store.service.implementation;

import com.yuseogi.pos.domain.store.dto.request.CreateStoreRequestDto;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.repository.StoreRepository;
import com.yuseogi.pos.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public void createStore(CreateStoreRequestDto request) {
        StoreEntity store = request.toStoreEntity();

        storeRepository.save(store);
    }
}
