package com.yuseogi.userservice.service;

import com.yuseogi.userservice.infrastructure.security.dto.TokenInfoResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface UserAuthService {

    Authentication authenticateKakao(String kakaoAccessToken);

    TokenInfoResponseDto login(HttpServletRequest httpServletRequest, Authentication authentication);

    TokenInfoResponseDto reIssue(HttpServletRequest httpServletRequest, String refreshToken);

    void logout(HttpServletRequest httpServletRequest);
}
