package com.yuseogi.userservice.service.implementation;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.common.util.NetworkUtil;
import com.yuseogi.common.util.ParseRequestUtil;
import com.yuseogi.userservice.dto.UserAccountDto;
import com.yuseogi.userservice.exception.UserErrorCode;
import com.yuseogi.userservice.infrastructure.cache.redis.dao.InvalidAccessToken;
import com.yuseogi.userservice.infrastructure.cache.redis.dao.RefreshToken;
import com.yuseogi.userservice.infrastructure.cache.redis.repository.InvalidAccessTokenRedisRepository;
import com.yuseogi.userservice.infrastructure.cache.redis.repository.RefreshTokenRedisRepository;
import com.yuseogi.userservice.infrastructure.client.KakaoWebClient;
import com.yuseogi.userservice.infrastructure.client.dto.response.KakaoAccountResponseDto;
import com.yuseogi.userservice.infrastructure.security.ExpireTime;
import com.yuseogi.userservice.infrastructure.security.dto.TokenInfoResponseDto;
import com.yuseogi.userservice.infrastructure.security.jwt.component.JwtProvider;
import com.yuseogi.userservice.service.UserAccountService;
import com.yuseogi.userservice.service.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final JwtProvider jwtProvider;
    private final KakaoWebClient kakaoWebClient;

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final InvalidAccessTokenRedisRepository invalidAccessTokenRedisRepository;

    private final UserAccountService userAccountService;

    @Override
    public UserAccountDto authenticateKakao(String kakaoAccessToken) {
        KakaoAccountResponseDto kakaoAccountResponse = kakaoWebClient.getAccount("Bearer " + kakaoAccessToken);

        return userAccountService.getUser(kakaoAccountResponse.kakao_account().email());
    }

    @Override
    public TokenInfoResponseDto login(HttpServletRequest httpServletRequest, UserAccountDto userAccountDto) {
        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfoResponseDto tokenInfoResponse = jwtProvider.generateToken(userAccountDto.id(), List.of(userAccountDto.role()));

        // RefreshToken 을 Redis 에 저장
        refreshTokenRedisRepository.save(RefreshToken.builder()
            .id(userAccountDto.id())
            .ip(NetworkUtil.getClientIp(httpServletRequest))
            .authorityList(tokenInfoResponse.authorityList())
            .refreshToken(tokenInfoResponse.refreshToken())
            .expireIn(ExpireTime.REFRESH_TOKEN.getSecond())
            .build());

        return tokenInfoResponse;
    }

    @Override
    public TokenInfoResponseDto reIssue(HttpServletRequest httpServletRequest, String refreshToken) {
        // 1. refresh token 인지 확인
        if (StringUtils.hasText(refreshToken) && jwtProvider.validateToken(refreshToken)) {
            jwtProvider.validateTokenType(refreshToken, JwtProvider.TYPE_REFRESH);
            Optional<RefreshToken> optionalRefreshToken = refreshTokenRedisRepository.findByRefreshToken(refreshToken);
            if (optionalRefreshToken.isPresent()) {
                RefreshToken savedRefreshToken = optionalRefreshToken.get();
                // 2. 최초 로그인한 ip 와 같은지 확인 (처리 방식에 따라 재발급을 하지 않거나 메일 등의 알림을 주는 방법이 있음)
                String currentIpAddress = NetworkUtil.getClientIp(httpServletRequest);
                if (savedRefreshToken.getIp().equals(currentIpAddress)) {
                    // 3. Redis 에 저장된 RefreshToken 정보를 기반으로 JWT Token 생성
                    TokenInfoResponseDto response = jwtProvider.generateToken(savedRefreshToken.getId(), savedRefreshToken.getAuthorityList());

                    // 4. Redis RefreshToken update
                    refreshTokenRedisRepository.save(RefreshToken.builder()
                        .id(savedRefreshToken.getId())
                        .ip(currentIpAddress)
                        .authorityList(response.authorityList())
                        .refreshToken(response.refreshToken())
                        .expireIn(ExpireTime.REFRESH_TOKEN.getSecond())
                        .build());

                    return response;
                }
            }
        }

        throw new CustomException(UserErrorCode.INVALID_REFRESH_TOKEN);
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest) {
        Long userId = ParseRequestUtil.extractUserIdFromRequest(httpServletRequest);
        String accessToken = ParseRequestUtil.extractAccessTokenFromRequest(httpServletRequest);

        // 2. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        refreshTokenRedisRepository.deleteById(userId);
        // 3. Redis 에서 해당 Access Token 을 Black List 로 저장합니다.
        invalidAccessTokenRedisRepository.save(InvalidAccessToken.builder()
            .id(userId)
            .accessToken(accessToken)
            .expireIn(jwtProvider.getExpireIn(accessToken))
            .build());
    }
}