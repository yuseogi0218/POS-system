package com.yuseogi.pos.domain.user.service;

import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import com.yuseogi.pos.domain.user.service.dto.response.KakaoAccountResponseDto;
import com.yuseogi.pos.domain.user.controller.dto.response.LoginKakaoResponseDto;
import org.springframework.security.core.Authentication;

public interface UserAuthService {

    Authentication authenticateKakao(String kakaoAccessToken);

    KakaoAccountResponseDto getKakaoAccount(String kakaoAccessToken);

    TokenInfoResponseDto login(Authentication authentication);
}
