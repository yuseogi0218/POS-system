package com.yuseogi.pos.domain.user.unit.repository;

import com.yuseogi.pos.common.RepositoryUnitTest;
import com.yuseogi.pos.domain.user.entity.UserEntity;
import com.yuseogi.pos.domain.user.entity.UserEntityBuilder;
import com.yuseogi.pos.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class UserRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findFirstByEmail_존재_O() {
        // given
        UserEntity expectedUser = UserEntityBuilder.build();

        // when
        Optional<UserEntity> optionalUser = userRepository.findFirstByEmail(expectedUser.getEmail());

        // then
        Assertions.assertThat(optionalUser.isPresent()).isTrue();
        optionalUser.ifPresent(
            actualUser -> UserEntityBuilder.assertUser(actualUser, expectedUser)
        );
    }

    @Test
    void findFirstByEmail_존재_X() {
        // given
        String unknownUserEmail = "unknown-user@domain.com";

        // when
        Optional<UserEntity> optionalUser = userRepository.findFirstByEmail(unknownUserEmail);

        // then
        Assertions.assertThat(optionalUser.isEmpty()).isTrue();
    }
}
