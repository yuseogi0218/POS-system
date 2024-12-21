package com.yuseogi.pos.domain.store.service.implementation;

import com.yuseogi.pos.domain.store.dto.request.CreateStoreRequestDto;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.repository.StoreRepository;
import com.yuseogi.pos.domain.store.repository.TradeDeviceRepository;
import com.yuseogi.pos.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final TradeDeviceRepository tradeDeviceRepository;
    private final StoreRepository storeRepository;

    @Override
    public void createStore(CreateStoreRequestDto request) {
        StoreEntity store = request.toStoreEntity();

        StoreEntity savedStore = storeRepository.save(store);

        int tradeDeviceCount = savedStore.getPosGrade().getTradeDeviceCount();
        List<TradeDeviceEntity> tradeDeviceList = IntStream.range(0, tradeDeviceCount)
            .mapToObj(i -> TradeDeviceEntity.builder()
                .store(savedStore)
                .build())
            .collect(Collectors.toList());

        tradeDeviceRepository.saveAll(tradeDeviceList);
    }
}
