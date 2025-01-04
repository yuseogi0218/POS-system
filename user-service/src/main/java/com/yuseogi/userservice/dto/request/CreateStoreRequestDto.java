package com.yuseogi.userservice.dto.request;

import com.yuseogi.userservice.entity.UserEntity;

public record CreateStoreRequestDto(
    Long userId,
    String storeName,
    String posGrade,
    Integer settlementDate
) {
    public static CreateStoreRequestDto from(UserEntity userEntity, SignUpKakaoRequestDto request) {
        return new CreateStoreRequestDto(userEntity.getId(), request.storeName(), request.posGrade(), request.settlementDate());
    }
}
