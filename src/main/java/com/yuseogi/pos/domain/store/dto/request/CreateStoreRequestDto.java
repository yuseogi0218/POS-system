package com.yuseogi.pos.domain.store.dto.request;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.type.PosGrade;
import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.pos.domain.user.entity.UserEntity;
import lombok.Builder;

@Builder
public record CreateStoreRequestDto(
    UserEntity user,
    String storeName,
    String posGrade
) {
    public static CreateStoreRequestDto from(UserEntity userEntity, SignUpKakaoRequestDto request) {
        return CreateStoreRequestDto.builder()
            .user(userEntity)
            .storeName(request.storeName())
            .posGrade(request.posGrade())
            .build();
    }

    public StoreEntity toStoreEntity() {
        return StoreEntity.builder()
            .ownerUser(user)
            .name(storeName)
            .posGrade(PosGrade.ofRequest(posGrade))
            .build();
    }
}
