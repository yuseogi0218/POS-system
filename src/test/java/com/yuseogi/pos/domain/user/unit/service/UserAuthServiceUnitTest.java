package com.yuseogi.pos.domain.user.unit.service;

import com.yuseogi.common.util.NetworkUtil;
import com.yuseogi.pos.gateway.ServiceUnitTest;
import com.yuseogi.pos.gateway.cache.redis.dao.RefreshToken;
import com.yuseogi.pos.gateway.cache.redis.repository.InvalidAccessTokenRedisRepository;
import com.yuseogi.pos.gateway.cache.redis.repository.RefreshTokenRedisRepository;
import com.yuseogi.pos.gateway.client.KakaoWebClient;
import com.yuseogi.pos.gateway.client.dto.response.KakaoAccountResponseDto;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.pos.gateway.security.dto.TokenInfoResponseDto;
import com.yuseogi.pos.gateway.security.jwt.component.JwtProvider;
import com.yuseogi.pos.domain.user.exception.UserErrorCode;
import com.yuseogi.pos.domain.user.service.implementation.UserAuthServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserAuthServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private KakaoWebClient kakaoWebClient;

    @Mock
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Mock
    private InvalidAccessTokenRedisRepository invalidAccessTokenRedisRepository;

    private MockHttpServletRequest httpServletRequest;

    @BeforeEach
    public void setUpForEach() {
        httpServletRequest = new MockHttpServletRequest();
    }

    /**
     * kakaoAccessToken 을 이용한 인증 성공
     */
    @Test
    void authenticateKakao_성공() {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        KakaoAccountResponseDto kakaoAccountResponse = mock(KakaoAccountResponseDto.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        Authentication expectedAuthentication = mock(Authentication.class);

        // stub
        when(kakaoWebClient.getAccount(kakaoAccessToken)).thenReturn(kakaoAccountResponse);
        when(kakaoAccountResponse.toAuthentication()).thenReturn(usernamePasswordAuthenticationToken);
        when(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).thenReturn(expectedAuthentication);

        // when
        Authentication actualAuthentication = userAuthService.authenticateKakao(kakaoAccessToken);

        // then
        Assertions.assertThat(actualAuthentication).isEqualTo(expectedAuthentication);
    }

    /**
     * 로그인 성공
     */
    @Test
    void login_성공() {
        // given
        Authentication authentication = mock(Authentication.class);
        String email = "user@domain.com";
        TokenInfoResponseDto expectedTokenInfoResponse = mock(TokenInfoResponseDto.class);

        // stub
        when(authentication.getName()).thenReturn(email);
        when(jwtProvider.generateToken(eq(email), any())).thenReturn(expectedTokenInfoResponse);

        // when
        TokenInfoResponseDto actualTokenInfoResponse = userAuthService.login(httpServletRequest, authentication);

        // then
        Assertions.assertThat(actualTokenInfoResponse).isEqualTo(expectedTokenInfoResponse);
        verify(refreshTokenRedisRepository, times(1)).save(any());
    }

    /**
     * Refresh Token 을 이용한 Access Token 재발급 (Re Issue) 성공
     */
    @Test
    void reIssue_성공() {
        // given
        String email = "user@domain.com";
        String refreshTokenValue = "refreshToken";
        RefreshToken refreshToken = mock(RefreshToken.class);
        TokenInfoResponseDto expectedTokenInfoResponse = mock(TokenInfoResponseDto.class);

        // stub
        when(jwtProvider.validateToken(refreshTokenValue)).thenReturn(true);
        when(refreshTokenRedisRepository.findByRefreshToken(refreshTokenValue)).thenReturn(Optional.of(refreshToken));
        when(refreshToken.getIp()).thenReturn(NetworkUtil.getClientIp(httpServletRequest));
        when(refreshToken.getId()).thenReturn(email);
        when(jwtProvider.generateToken(eq(email), any())).thenReturn(expectedTokenInfoResponse);

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
        String email = "user@domain.com";
        String accessToken = "accessToken";
        Authentication authentication = mock(Authentication.class);

        // stub
        httpServletRequest.setAttribute("resolvedToken", accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(email);


        // when
        userAuthService.logout(httpServletRequest);

        // then
        verify(refreshTokenRedisRepository, times(1)).deleteById(email);
        verify(invalidAccessTokenRedisRepository, times(1)).save(any());
    }
}
