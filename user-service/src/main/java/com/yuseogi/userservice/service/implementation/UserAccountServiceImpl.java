package com.yuseogi.userservice.service.implementation;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.userservice.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.userservice.entity.UserEntity;
import com.yuseogi.userservice.exception.UserErrorCode;
import com.yuseogi.userservice.infrastructure.client.KakaoWebClient;
import com.yuseogi.userservice.infrastructure.client.dto.response.KakaoAccountResponseDto;
import com.yuseogi.userservice.repository.UserRepository;
import com.yuseogi.userservice.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final KakaoWebClient kakaoWebClient;

    private final UserRepository userRepository;

    //TODO: 2024-12-28 의존성 순환에 의해 주석 처리
//    private final StoreService storeService;

    @Transactional
    @Override
    public void signUpKakao(String kakaoAccessToken, SignUpKakaoRequestDto request) {
        KakaoAccountResponseDto kakaoAccountResponse = kakaoWebClient.getAccount(kakaoAccessToken);

        UserEntity user = kakaoAccountResponse.toUserEntity();

        checkUsedEmail(user.getEmail());

        UserEntity savedUserEntity = userRepository.save(user);

        //TODO: 2024-12-28 의존성 순환에 의해 주석 처리
        // storeService.createStore(CreateStoreRequestDto.from(savedUserEntity, request));
    }

    private void checkUsedEmail(String email) {
        Optional<UserEntity> userEntityOptional = userRepository.findFirstByEmail(email);

        if (userEntityOptional.isPresent()) {
            throw new CustomException(UserErrorCode.EMAIL_ALREADY_USED);
        }
    }
}
