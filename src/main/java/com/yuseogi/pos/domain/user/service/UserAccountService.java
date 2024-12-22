package com.yuseogi.pos.domain.user.service;

import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDto;

public interface UserAccountService {

    void signUpKakao(String kakaoAccessToken, SignUpKakaoRequestDto request);

}
