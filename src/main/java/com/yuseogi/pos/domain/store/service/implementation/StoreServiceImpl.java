package com.yuseogi.pos.domain.store.service.implementation;

import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.domain.store.dto.request.CreateStoreRequestDto;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.exception.StoreErrorCode;
import com.yuseogi.pos.domain.store.repository.StoreRepository;
import com.yuseogi.pos.domain.store.repository.TradeDeviceRepository;
import com.yuseogi.pos.domain.store.service.StoreService;
import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final TradeDeviceService tradeDeviceService;

    @Transactional
    @Override
    public void createStore(CreateStoreRequestDto request) {
        StoreEntity store = request.toStoreEntity();

        StoreEntity savedStore = storeRepository.save(store);

        tradeDeviceService.createTradeDevice(savedStore);
    }

    @Override
    public StoreEntity getStore(String ownerUserEmail) {
        return storeRepository.findFirstByOwnerUserEmail(ownerUserEmail).orElseThrow(() -> new CustomException(StoreErrorCode.NOT_FOUND_STORE));
    }

    @Override
    public StoreEntity getStore(Long tradeDeviceId) {
        return storeRepository.findFirstByTradeDeviceId(tradeDeviceId);
    }
}
