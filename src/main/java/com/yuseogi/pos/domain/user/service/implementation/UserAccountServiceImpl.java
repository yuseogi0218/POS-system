package com.yuseogi.pos.domain.user.service.implementation;

import com.yuseogi.pos.gateway.client.KakaoWebClient;
import com.yuseogi.pos.gateway.client.dto.response.KakaoAccountResponseDto;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.pos.domain.store.dto.request.CreateStoreRequestDto;
import com.yuseogi.pos.domain.store.service.StoreService;
import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.pos.domain.user.entity.UserEntity;
import com.yuseogi.pos.domain.user.exception.UserErrorCode;
import com.yuseogi.pos.domain.user.repository.UserRepository;
import com.yuseogi.pos.domain.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final KakaoWebClient kakaoWebClient;

    private final UserRepository userRepository;

    private final StoreService storeService;

    @Transactional
    @Override
    public void signUpKakao(String kakaoAccessToken, SignUpKakaoRequestDto request) {
        KakaoAccountResponseDto kakaoAccountResponse = kakaoWebClient.getAccount(kakaoAccessToken);

        UserEntity user = kakaoAccountResponse.toUserEntity();

        checkUsedEmail(user.getEmail());

        UserEntity savedUserEntity = userRepository.save(user);

        storeService.createStore(CreateStoreRequestDto.from(savedUserEntity, request));
    }

    private void checkUsedEmail(String email) {
        Optional<UserEntity> userEntityOptional = userRepository.findFirstByEmail(email);

        if (userEntityOptional.isPresent()) {
            throw new CustomException(UserErrorCode.EMAIL_ALREADY_USED);
        }
    }
}
