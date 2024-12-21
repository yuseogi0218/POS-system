package com.yuseogi.pos.domain.user.service.implementation;

import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import com.yuseogi.pos.common.security.jwt.component.JwtProvider;
import com.yuseogi.pos.domain.user.service.dto.response.KakaoAccountResponseDto;
import com.yuseogi.pos.domain.user.controller.dto.response.LoginKakaoResponseDto;
import com.yuseogi.pos.domain.user.service.UserAuthService;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private String KAKAO_ACCOUNT_URI;

    public UserAuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider,
            @Value("${kakao.account-uri}") String KAKAO_ACCOUNT_URI
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
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
    public TokenInfoResponseDto login(Authentication authentication) {
        // 인증 정보를 기반으로 JWT 토큰 생성
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> authorityList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        TokenInfoResponseDto tokenInfoResponse = jwtProvider.generateToken(authentication.getName(), authorityList);
        return tokenInfoResponse;
    }
}