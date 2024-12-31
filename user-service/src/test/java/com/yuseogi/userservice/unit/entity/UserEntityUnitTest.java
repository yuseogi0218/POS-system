package com.yuseogi.userservice.unit.entity;

import com.yuseogi.userservice.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserEntityUnitTest {

    @Test
    void constructor() {
        // given
        String expectedEmail = "user@domain.com";
        String expectedName = "username";
        String expectedPhone = "01012345687";

        // when
        UserEntity actualUser = new UserEntity(expectedEmail, expectedName, expectedPhone);

        // then
        Assertions.assertThat(actualUser.getEmail()).isEqualTo(expectedEmail);
        Assertions.assertThat(actualUser.getName()).isEqualTo(expectedName);
        Assertions.assertThat(actualUser.getPhone()).isEqualTo(expectedPhone);
    }
}
