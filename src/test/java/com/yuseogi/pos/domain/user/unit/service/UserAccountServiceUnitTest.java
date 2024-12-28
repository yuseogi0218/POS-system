package com.yuseogi.pos.domain.user.unit.service;

import com.yuseogi.pos.gateway.ServiceUnitTest;
import com.yuseogi.pos.gateway.client.KakaoWebClient;
import com.yuseogi.pos.gateway.client.dto.response.KakaoAccountResponseDto;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.pos.domain.store.service.StoreService;
import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.pos.domain.user.entity.UserEntity;
import com.yuseogi.pos.domain.user.exception.UserErrorCode;
import com.yuseogi.pos.domain.user.repository.UserRepository;
import com.yuseogi.pos.domain.user.service.implementation.UserAccountServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.*;


public class UserAccountServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    @Mock
    private KakaoWebClient kakaoWebClient;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreService storeService;

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
        when(kakaoWebClient.getAccount(kakaoAccessToken)).thenReturn(kakaoAccountResponse);
        when(kakaoAccountResponse.toUserEntity()).thenReturn(user);
        when(user.getEmail()).thenReturn(email);
        when(userRepository.findFirstByEmail(email)).thenReturn(Optional.empty());

        // when
        userAccountService.signUpKakao(kakaoAccessToken, request);

        // then
        verify(userRepository, times(1)).save(any());
        verify(storeService, times(1)).createStore(any());
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
        when(kakaoWebClient.getAccount(kakaoAccessToken)).thenReturn(kakaoAccountResponse);
        when(kakaoAccountResponse.toUserEntity()).thenReturn(user);
        when(user.getEmail()).thenReturn(email);
        when(userRepository.findFirstByEmail(email)).thenReturn(Optional.of(user));

        // when & then
        Assertions.assertThatThrownBy(() -> userAccountService.signUpKakao(kakaoAccessToken, request))
            .isInstanceOf(CustomException.class)
            .hasMessage(UserErrorCode.EMAIL_ALREADY_USED.getMessage());
    }
}
