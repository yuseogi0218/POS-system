package com.yuseogi.userservice.service;

import com.yuseogi.userservice.dto.request.SignUpKakaoRequestDto;

public interface UserAccountService {

    void signUpKakao(String kakaoAccessToken, SignUpKakaoRequestDto request);

}
