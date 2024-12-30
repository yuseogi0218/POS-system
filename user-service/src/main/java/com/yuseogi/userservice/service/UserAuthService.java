package com.yuseogi.userservice.service;

import com.yuseogi.userservice.dto.UserAccountDto;
import com.yuseogi.userservice.infrastructure.security.dto.TokenInfoResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserAuthService {

    UserAccountDto authenticateKakao(String kakaoAccessToken);

    TokenInfoResponseDto login(HttpServletRequest httpServletRequest, UserAccountDto userAccountDto);

    TokenInfoResponseDto reIssue(HttpServletRequest httpServletRequest, String refreshToken);

    void logout(HttpServletRequest httpServletRequest);
}
