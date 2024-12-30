package com.yuseogi.storeservice.dto.request;

import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.type.PosGrade;
import com.yuseogi.userservice.entity.UserEntity;

public record CreateStoreRequestDto(
    Long userId,
    String storeName,
    String posGrade
) {

    public StoreEntity toStoreEntity() {
        UserEntity user = new UserEntity(userId);

        return StoreEntity.builder()
            .ownerUser(user)
            .name(storeName)
            .posGrade(PosGrade.valueOf(posGrade))
            .build();
    }
}
