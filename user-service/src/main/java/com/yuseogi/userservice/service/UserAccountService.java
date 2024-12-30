package com.yuseogi.userservice.service;

import com.yuseogi.userservice.dto.UserAccountDto;
import com.yuseogi.userservice.dto.request.SignUpKakaoRequestDto;

public interface UserAccountService {

    UserAccountDto getUser(String email);

    void signUpKakao(String kakaoAccessToken, SignUpKakaoRequestDto request);

}
