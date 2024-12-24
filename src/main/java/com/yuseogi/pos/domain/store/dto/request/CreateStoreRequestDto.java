package com.yuseogi.pos.domain.store.dto.request;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.type.PosGrade;
import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.pos.domain.user.entity.UserEntity;
import lombok.Builder;

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
