package com.yuseogi.pos.domain.user.entity;

import org.assertj.core.api.Assertions;

public class UserEntityBuilder {

    public static UserEntity build() {
        return UserEntity.builder()
            .email("user@domain.com")
            .name("username")
            .phone("01012345678")
            .build();
    }

    public static void assertUser(UserEntity actualUser, UserEntity expectedUser) {
        Assertions.assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        Assertions.assertThat(actualUser.getName()).isEqualTo(expectedUser.getName());
        Assertions.assertThat(actualUser.getPhone()).isEqualTo(expectedUser.getPhone());
    }
}
