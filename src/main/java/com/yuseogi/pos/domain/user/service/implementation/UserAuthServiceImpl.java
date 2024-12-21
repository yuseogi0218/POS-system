package com.yuseogi.pos.domain.user.service.implementation;

import com.yuseogi.pos.common.cache.redis.dao.RefreshToken;
import com.yuseogi.pos.common.cache.redis.repository.RefreshTokenRedisRepository;
import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import com.yuseogi.pos.common.security.jwt.component.JwtProvider;
import com.yuseogi.pos.common.util.NetworkUtil;
import com.yuseogi.pos.domain.user.service.dto.response.KakaoAccountResponseDto;
import com.yuseogi.pos.domain.user.service.UserAuthService;
import io.netty.handler.codec.http.HttpHeaderValues;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final String KAKAO_ACCOUNT_URI;

    public UserAuthServiceImpl(
        AuthenticationManager authenticationManager,
        JwtProvider jwtProvider,
        RefreshTokenRedisRepository refreshTokenRedisRepository,
        @Value("${kakao.account-uri}") String KAKAO_ACCOUNT_URI
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
        this.KAKAO_ACCOUNT_URI = KAKAO_ACCOUNT_URI;
    }

    @Override
    public Authentication authenticateKakao(String kakaoAccessToken) {
        KakaoAccountResponseDto kakaoAccountResponse = getKakaoAccount(kakaoAccessToken);

        Authentication authentication = authenticationManager.authenticate(kakaoAccountResponse.toAuthentication());

        return authentication;
    }

    @Override
    public KakaoAccountResponseDto getKakaoAccount(String kakaoAccessToken) {
        return WebClient.create(KAKAO_ACCOUNT_URI)
                .get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR)))
                .bodyToMono(KakaoAccountResponseDto.class)
                .block();
    }

    @Override
    public TokenInfoResponseDto login(HttpServletRequest httpServletRequest, Authentication authentication) {
        // 인증 정보를 기반으로 JWT 토큰 생성
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> authorityList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        TokenInfoResponseDto tokenInfoResponse = jwtProvider.generateToken(authentication.getName(), authorityList);

        // RefreshToken 을 Redis 에 저장
        refreshTokenRedisRepository.save(RefreshToken.builder()
            .id(authentication.getName())
            .ip(NetworkUtil.getClientIp(httpServletRequest))
            .authorityList(tokenInfoResponse.authorityList())
            .refreshToken(tokenInfoResponse.refreshToken())
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
                        .build());

                    return response;
                }
            }
        }

        throw new CustomException(CommonErrorCode.INVALID_REFRESH_TOKEN);
    }
}