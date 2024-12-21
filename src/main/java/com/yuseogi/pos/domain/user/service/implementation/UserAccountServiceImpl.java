package com.yuseogi.pos.domain.user.service.implementation;

import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.pos.domain.user.entity.UserEntity;
import com.yuseogi.pos.domain.user.exception.UserErrorCode;
import com.yuseogi.pos.domain.user.repository.UserRepository;
import com.yuseogi.pos.domain.user.service.UserAccountService;
import com.yuseogi.pos.domain.user.service.UserAuthService;
import com.yuseogi.pos.domain.user.service.dto.response.KakaoAccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;

    private final UserAuthService userAuthService;

    @Transactional
    @Override
    public void signUpKakao(String kakaoAccessToken, SignUpKakaoRequestDto request) {
        KakaoAccountResponseDto kakaoAccountResponse = userAuthService.getKakaoAccount(kakaoAccessToken);

        UserEntity userEntity = kakaoAccountResponse.toUserEntity();

        checkUsedEmail(userEntity.getEmail());

        userRepository.save(userEntity);
    }

    @Override
    public void checkUsedEmail(String email) {
        Optional<UserEntity> userEntityOptional = userRepository.findFirstByEmail(email);

        if (userEntityOptional.isPresent()) {
            throw new CustomException(UserErrorCode.EMAIL_ALREADY_USED);
        }
    }
}
