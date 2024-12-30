package com.yuseogi.userservice.dto;

import com.yuseogi.userservice.entity.UserEntity;

public record UserAccountDto(
    Long id,
    String email,
    String role
) {
    public static UserAccountDto fromEntity(UserEntity user) {
        return new UserAccountDto(user.getId(), user.getEmail(), "ROLE_STORE_OWNER");
    }
}
