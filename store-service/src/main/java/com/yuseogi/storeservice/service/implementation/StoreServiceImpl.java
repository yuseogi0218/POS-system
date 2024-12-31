package com.yuseogi.storeservice.service.implementation;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.dto.request.CreateStoreRequestDto;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.exception.StoreErrorCode;
import com.yuseogi.storeservice.repository.StoreRepository;
import com.yuseogi.storeservice.service.StoreService;
import com.yuseogi.storeservice.service.TradeDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public StoreEntity getStore(Long storeId) {
        return storeRepository.findFirstById(storeId).orElseThrow(() -> new CustomException(StoreErrorCode.NOT_FOUND_STORE));
    }

    @Override
    public StoreEntity getStoreByOwnerUser(Long ownerUserId) {
        return storeRepository.findFirstByOwnerUser(ownerUserId).orElseThrow(() -> new CustomException(StoreErrorCode.NOT_FOUND_STORE));
    }

}
