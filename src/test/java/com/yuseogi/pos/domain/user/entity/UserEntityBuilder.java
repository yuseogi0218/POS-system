package com.yuseogi.pos.domain.user.entity;

public class UserEntityBuilder {

    public static UserEntity build() {
        return UserEntity.builder()
            .email("user1@gmail.com")
            .name("user-name")
            .phone("01012345678")
            .build();
    }
}
