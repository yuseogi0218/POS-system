package com.yuseogi.storeservice.dto.request;

import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.type.PosGrade;

public record CreateStoreRequestDto(
    Long userId,
    String storeName,
    String posGrade
) {

    public StoreEntity toStoreEntity() {
        return StoreEntity.builder()
            .ownerUserId(userId)
            .name(storeName)
            .posGrade(PosGrade.valueOf(posGrade))
            .build();
    }
}
