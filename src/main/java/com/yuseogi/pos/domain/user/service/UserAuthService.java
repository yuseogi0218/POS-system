package com.yuseogi.pos.domain.user.service;

import com.yuseogi.pos.domain.user.dto.response.KakaoAccountResponseDto;
import com.yuseogi.pos.domain.user.dto.response.LoginResponseDto;
import org.springframework.security.core.Authentication;

public interface UserAuthService {

    Authentication authenticateKakao(String kakaoAccessToken);

    KakaoAccountResponseDto getKakaoAccount(String kakaoAccessToken);

    LoginResponseDto login(Authentication authentication);
}
