package com.yuseogi.storeservice.dto.request;

import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.type.PosGrade;
import com.yuseogi.userservice.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.userservice.entity.UserEntity;

public record CreateStoreRequestDto(
    UserEntity user,
    String storeName,
    String posGrade
) {
    public static CreateStoreRequestDto from(UserEntity userEntity, SignUpKakaoRequestDto request) {
        return new CreateStoreRequestDto(userEntity, request.storeName(), request.posGrade());
    }

    public StoreEntity toStoreEntity() {
        return StoreEntity.builder()
            .ownerUser(user)
            .name(storeName)
            .posGrade(PosGrade.valueOf(posGrade))
            .build();
    }
}
