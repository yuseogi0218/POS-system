package com.yuseogi.userservice.service;

import com.yuseogi.userservice.dto.UserAccountDto;
import com.yuseogi.userservice.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.userservice.entity.UserEntity;

public interface UserAccountService {

    UserAccountDto getUser(String email);

    UserEntity signUpKakao(String kakaoAccessToken, SignUpKakaoRequestDto request);

}
