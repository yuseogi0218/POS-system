package com.yuseogi.storeservice.service.implementation;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import com.yuseogi.storeservice.exception.StoreErrorCode;
import com.yuseogi.storeservice.repository.TradeDeviceRepository;
import com.yuseogi.storeservice.service.TradeDeviceService;
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
    public TradeDeviceEntity getTradeDevice(Long tradeDeviceId) {
        return tradeDeviceRepository.findFirstById(tradeDeviceId).orElseThrow(() -> new CustomException(StoreErrorCode.NOT_FOUND_TRADE_DEVICE));
    }

    @Override
    public void checkExistTradeDevice(Long tradeDeviceId) {
        if (!tradeDeviceRepository.existsById(tradeDeviceId)) {
            throw new CustomException(StoreErrorCode.NOT_FOUND_TRADE_DEVICE);
        }
    }

    @Override
    public void createTradeDevice(StoreEntity store) {
        int tradeDeviceCount = store.getPosGrade().getTradeDeviceCount();
        List<TradeDeviceEntity> tradeDeviceList = IntStream.range(0, tradeDeviceCount)
            .mapToObj(i -> TradeDeviceEntity.builder()
                .store(store)
                .build())
            .collect(Collectors.toList());

        tradeDeviceRepository.saveAll(tradeDeviceList);
    }

    @Override
    public List<Long> getTradeDeviceList(StoreEntity store) {
        return tradeDeviceRepository.findAllIdByStore(store.getId());
    }
}
