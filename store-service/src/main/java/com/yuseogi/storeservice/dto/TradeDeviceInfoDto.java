package com.yuseogi.storeservice.dto;

import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import lombok.Builder;

public record TradeDeviceInfoDto(
    Long id,
    Long storeId
) {

    public TradeDeviceInfoDto(TradeDeviceEntity tradeDevice) {
        this(
            tradeDevice.getId(),
            tradeDevice.getStore().getId()
        );
    }
}
