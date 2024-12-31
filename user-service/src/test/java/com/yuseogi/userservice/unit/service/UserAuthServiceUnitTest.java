package com.yuseogi.userservice.unit.service;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.userservice.dto.UserAccountDto;
import com.yuseogi.userservice.exception.UserErrorCode;
import com.yuseogi.userservice.infrastructure.cache.redis.dao.RefreshToken;
import com.yuseogi.userservice.infrastructure.cache.redis.repository.InvalidAccessTokenRedisRepository;
import com.yuseogi.userservice.infrastructure.cache.redis.repository.RefreshTokenRedisRepository;
import com.yuseogi.userservice.infrastructure.client.KakaoWebClient;
import com.yuseogi.userservice.infrastructure.client.dto.response.KakaoAccountResponseDto;
import com.yuseogi.userservice.infrastructure.security.dto.TokenInfoResponseDto;
import com.yuseogi.userservice.infrastructure.security.jwt.component.JwtProvider;
import com.yuseogi.userservice.service.UserAccountService;
import com.yuseogi.userservice.service.implementation.UserAuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserAuthServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private KakaoWebClient kakaoWebClient;

    @Mock
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Mock
    private InvalidAccessTokenRedisRepository invalidAccessTokenRedisRepository;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private HttpServletRequest httpServletRequest;

    /**
     * kakaoAccessToken 을 이용한 인증 성공
     */
    @Test
    void authenticateKakao_성공() {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        KakaoAccountResponseDto kakaoAccountResponse = mock(KakaoAccountResponseDto.class);
        KakaoAccountResponseDto.KakaoAccount kakaoAccount = mock(KakaoAccountResponseDto.KakaoAccount.class);
        String email = "user@domain.com";
        UserAccountDto expectedUserAccountDto = mock(UserAccountDto.class);

        // stub
        when(kakaoWebClient.getAccount("Bearer " + kakaoAccessToken)).thenReturn(kakaoAccountResponse);
        when(kakaoAccountResponse.kakao_account()).thenReturn(kakaoAccount);
        when(kakaoAccount.email()).thenReturn(email);
        when(userAccountService.getUser(email)).thenReturn(expectedUserAccountDto);

        // when
        UserAccountDto actualUserAccountDto = userAuthService.authenticateKakao(kakaoAccessToken);

        // then
        Assertions.assertThat(actualUserAccountDto).isEqualTo(expectedUserAccountDto);
    }

    /**
     * 로그인 성공
     */
    @Test
    void login_성공() {
        // given
        UserAccountDto userAccountDto = mock(UserAccountDto.class);
        Long userId = 1L;
        String role = "ROLE_USER";
        TokenInfoResponseDto expectedTokenInfoResponse = mock(TokenInfoResponseDto.class);

        // stub
        when(userAccountDto.id()).thenReturn(userId);
        when(userAccountDto.role()).thenReturn(role);
        when(jwtProvider.generateToken(userId, List.of(role))).thenReturn(expectedTokenInfoResponse);

        // when
        TokenInfoResponseDto actualTokenInfoResponse = userAuthService.login(httpServletRequest, userAccountDto);

        // then
        Assertions.assertThat(actualTokenInfoResponse).isEqualTo(expectedTokenInfoResponse);
        verify(refreshTokenRedisRepository, times(1)).save(any(RefreshToken.class));
    }

    /**
     * Refresh Token 을 이용한 Access Token 재발급 (Re Issue) 성공
     */
    @Test
    void reIssue_성공() {
        // given
        String clientIp = "127.0.0.1";
        Long userId = 1L;
        String refreshTokenValue = "refreshToken";
        RefreshToken refreshToken = mock(RefreshToken.class);
        TokenInfoResponseDto expectedTokenInfoResponse = mock(TokenInfoResponseDto.class);

        // stub
        when(jwtProvider.validateToken(refreshTokenValue)).thenReturn(true);
        when(httpServletRequest.getHeader("X-Forwarded-For")).thenReturn(clientIp);
        when(refreshTokenRedisRepository.findByRefreshToken(refreshTokenValue)).thenReturn(Optional.of(refreshToken));
        when(refreshToken.getIp()).thenReturn(clientIp);
        when(refreshToken.getId()).thenReturn(userId);
        when(jwtProvider.generateToken(eq(userId), any())).thenReturn(expectedTokenInfoResponse);

        // when
        TokenInfoResponseDto actualTokenInfoResponse = userAuthService.reIssue(httpServletRequest, refreshTokenValue);

        // then
        Assertions.assertThat(actualTokenInfoResponse).isEqualTo(expectedTokenInfoResponse);
        verify(refreshTokenRedisRepository, times(1)).save(any());
    }

    /**
     * Refresh Token 을 이용한 Access Token 재발급 (Re Issue) 실패
     * - 실패 사유 : Refresh Token 의 값이 비어있음
     */
    @Test
    void reIssue_실패_INVALID_REFRESH_TOKEN_Refresh_Token_값_존재_X() {
        // given
        String emptyRefreshTokenValue = "";

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.reIssue(httpServletRequest, emptyRefreshTokenValue))
            .isInstanceOf(CustomException.class)
            .hasMessage(UserErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    /**
     * Refresh Token 을 이용한 Access Token 재발급 (Re Issue) 실패
     * - 실패 사유 : 유효하지 않은 Refresh Token 사용
     */
    @Test
    void reIssue_실패_INVALID_REFRESH_TOKEN_유효하지_않은_Refresh_Token_값() {
        // given
        String invalidRefreshTokenValue = "invalidRefreshToken";

        // stub
        when(jwtProvider.validateToken(invalidRefreshTokenValue)).thenReturn(false);

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.reIssue(httpServletRequest, invalidRefreshTokenValue))
            .isInstanceOf(CustomException.class)
            .hasMessage(UserErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    /**
     * Refresh Token 을 이용한 Access Token 재발급 (Re Issue) 실패
     * - 실패 사유 : refreshTokenRedisRepository 에 존재하지 않는 Refresh Token 사용
     */
    @Test
    void reIssue_실패_INVALID_REFRESH_TOKEN_refreshTokenRedisRepository_존재_X() {
        // given
        String unknownRefreshTokenValue = "unknownRefreshToken";

        // stub
        when(jwtProvider.validateToken(unknownRefreshTokenValue)).thenReturn(true);
        when(refreshTokenRedisRepository.findByRefreshToken(unknownRefreshTokenValue)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.reIssue(httpServletRequest, unknownRefreshTokenValue))
            .isInstanceOf(CustomException.class)
            .hasMessage(UserErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    /**
     * Refresh Token 을 이용한 Access Token 재발급 (Re Issue) 실패
     * - 실패 사유 : 최초 로그인한 IP 와 다른 IP 로부터의 요청
     */
    @Test
    void reIssue_실패_INVALID_REFRESH_TOKEN_IP_Address() {
        // given
        String refreshTokenValue = "refreshToken";
        RefreshToken refreshToken = mock(RefreshToken.class);
        String refreshTokenIpAddress = "256.100.100.100";

        // stub
        when(jwtProvider.validateToken(refreshTokenValue)).thenReturn(true);
        when(refreshTokenRedisRepository.findByRefreshToken(refreshTokenValue)).thenReturn(Optional.of(refreshToken));
        when(refreshToken.getIp()).thenReturn(refreshTokenIpAddress);

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.reIssue(httpServletRequest, refreshTokenValue))
            .isInstanceOf(CustomException.class)
            .hasMessage(UserErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 성공
     */
    @Test
    void logout_성공() {
        // given
        Long userId = 1L;
        String accessToken = "accessToken";

        // stub
        when(httpServletRequest.getHeader("X-Authorization-userId")).thenReturn(String.valueOf(userId));
        when(httpServletRequest.getHeader("X-Authorization-accessToken")).thenReturn(accessToken);

        // when
        userAuthService.logout(httpServletRequest);

        // then
        verify(refreshTokenRedisRepository, times(1)).deleteById(userId);
        verify(invalidAccessTokenRedisRepository, times(1)).save(any());
    }
}
