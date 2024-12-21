package com.yuseogi.pos.domain.user.service;

import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import com.yuseogi.pos.domain.user.service.dto.response.KakaoAccountResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface UserAuthService {

    Authentication authenticateKakao(String kakaoAccessToken);

    KakaoAccountResponseDto getKakaoAccount(String kakaoAccessToken);

    TokenInfoResponseDto login(HttpServletRequest httpServletRequest, Authentication authentication);

    TokenInfoResponseDto reIssue(HttpServletRequest httpServletRequest, String refreshToken);

    void logout(HttpServletRequest httpServletRequest);
}
