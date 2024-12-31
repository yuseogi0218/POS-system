package com.yuseogi.userservice.unit.service;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.userservice.dto.UserAccountDto;
import com.yuseogi.userservice.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.userservice.entity.UserEntity;
import com.yuseogi.userservice.exception.UserErrorCode;
import com.yuseogi.userservice.infrastructure.client.KakaoWebClient;
import com.yuseogi.userservice.infrastructure.client.dto.response.KakaoAccountResponseDto;
import com.yuseogi.userservice.repository.UserRepository;
import com.yuseogi.userservice.service.implementation.UserAccountServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;


public class UserAccountServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    @Mock
    private KakaoWebClient kakaoWebClient;

    @Mock
    private UserRepository userRepository;

    /**
     * 이메일 기준으로 사용자 정보 조회 성공
     */
    @Test
    void getUser_성공() {
        // given
        UserEntity user = mock(UserEntity.class);
        Long userId = 1L;
        String userEmail = "user@domain.com";

        when(user.getId()).thenReturn(userId);
        when(user.getEmail()).thenReturn(userEmail);
        UserAccountDto expectedUserAccountDto = UserAccountDto.fromEntity(user);

        // stub
        when(userRepository.findFirstByEmail(userEmail)).thenReturn(Optional.of(user));

        // when
        UserAccountDto actualUserAccountDto = userAccountService.getUser(userEmail);

        // then
        Assertions.assertThat(actualUserAccountDto).isEqualTo(expectedUserAccountDto);
    }

    /**
     * 이메일 기준으로 사용자 정보 조회 실패
     * - 실패 사유 : 존재하지 않는 사용
     */
    @Test
    void getUser_실패_NOT_FOUND_USER() {
        // given
        String unknownUserEmail = "unknown-user@domain.com";

        // stub
        when(userRepository.findFirstByEmail(unknownUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> userAccountService.getUser(unknownUserEmail))
            .isInstanceOf(CustomException.class)
            .hasMessage(UserErrorCode.NOT_FOUND_USER.getMessage());
    }

    /**
     * 소셜(카카오) 회원가입 성공
     */
    @Test
    void signUpKakao_성공() {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto request = mock(SignUpKakaoRequestDto.class);

        KakaoAccountResponseDto kakaoAccountResponse = mock(KakaoAccountResponseDto.class);
        UserEntity user = mock(UserEntity.class);
        String email = "user@domain.com";

        // stub
        when(kakaoWebClient.getAccount("Bearer " + kakaoAccessToken)).thenReturn(kakaoAccountResponse);
        when(kakaoAccountResponse.toUserEntity()).thenReturn(user);
        when(user.getEmail()).thenReturn(email);
        when(userRepository.findFirstByEmail(email)).thenReturn(Optional.empty());

        // when
        userAccountService.signUpKakao(kakaoAccessToken, request);

        // then
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    /**
     * 소셜(카카오) 회원가입 실패
     * - 실패 사유 : 이미 사용 중인 이메일 입니다.
     */
    @Test
    void signUpKakao_실패_EMAIL_ALREADY_USED() {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto request = mock(SignUpKakaoRequestDto.class);

        KakaoAccountResponseDto kakaoAccountResponse = mock(KakaoAccountResponseDto.class);
        UserEntity user = mock(UserEntity.class);
        String email = "user@domain.com";

        // stub
        when(kakaoWebClient.getAccount("Bearer " + kakaoAccessToken)).thenReturn(kakaoAccountResponse);
        when(kakaoAccountResponse.toUserEntity()).thenReturn(user);
        when(user.getEmail()).thenReturn(email);
        when(userRepository.findFirstByEmail(email)).thenReturn(Optional.of(user));

        // when & then
        Assertions.assertThatThrownBy(() -> userAccountService.signUpKakao(kakaoAccessToken, request))
            .isInstanceOf(CustomException.class)
            .hasMessage(UserErrorCode.EMAIL_ALREADY_USED.getMessage());
    }
}
