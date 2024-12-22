package com.yuseogi.pos.domain.user.unit.service;

import com.yuseogi.pos.common.ServiceUnitTest;
import com.yuseogi.pos.domain.user.entity.UserEntity;
import com.yuseogi.pos.domain.user.entity.UserEntityBuilder;
import com.yuseogi.pos.domain.user.exception.UserErrorCode;
import com.yuseogi.pos.domain.user.repository.UserRepository;
import com.yuseogi.pos.domain.user.service.implementation.UserDetailsServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class UserDetailsServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    /**
     * 이메일 기준으로 사용자 정보(UserDetails) 조회 성공
     */
    @Test
    void loadUserByUsername_성공() {
        // given
        UserEntity user = UserEntityBuilder.build();
        UserDetails expectedUserDetails = new User(user.getEmail(), user.getPassword(), user.getAuthorities());

        // stub
        when(userRepository.findFirstByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        UserDetails actualUserDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // then
        Assertions.assertThat(actualUserDetails).isEqualTo(expectedUserDetails);
    }

    /**
     * 이메일 기준으로 사용자 정보(UserDetails) 조회 실패
     * - 실패 사유 : 존재하지 않는 사용
     */
    @Test
    void loadUserByUsername_실패_NOT_FOUND_USER() {
        // given
        String unknownUserEmail = "unknown-user@domain.com";

        // stub
        when(userRepository.findFirstByEmail(unknownUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> userDetailsService.loadUserByUsername(unknownUserEmail))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage(UserErrorCode.NOT_FOUND_USER.getMessage());
    }
}
