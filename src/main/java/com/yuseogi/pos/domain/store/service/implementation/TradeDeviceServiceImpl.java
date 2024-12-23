package com.yuseogi.pos.domain.store.service.implementation;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.repository.TradeDeviceRepository;
import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class TradeDeviceServiceImpl implements TradeDeviceService {

    private final TradeDeviceRepository tradeDeviceRepository;

    @Override
    public void createTradeDevice(StoreEntity savedStore) {
        int tradeDeviceCount = savedStore.getPosGrade().getTradeDeviceCount();
        List<TradeDeviceEntity> tradeDeviceList = IntStream.range(0, tradeDeviceCount)
            .mapToObj(i -> TradeDeviceEntity.builder()
                .store(savedStore)
                .build())
            .collect(Collectors.toList());

        tradeDeviceRepository.saveAll(tradeDeviceList);
    }

    @Override
    public List<Long> getTradeDeviceList(StoreEntity store) {
        return tradeDeviceRepository.findAllIdByStoreId(store.getId());
    }
}
