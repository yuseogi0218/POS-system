package com.yuseogi.userservice.controller;

import com.yuseogi.userservice.dto.UserAccountDto;
import com.yuseogi.userservice.dto.request.CreateStoreRequestDto;
import com.yuseogi.userservice.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.userservice.entity.UserEntity;
import com.yuseogi.userservice.infrastructure.client.StoreServiceClient;
import com.yuseogi.userservice.infrastructure.security.dto.TokenInfoResponseDto;
import com.yuseogi.userservice.service.UserAccountService;
import com.yuseogi.userservice.service.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RequestMapping
@RestController
public class UserController {

    private final StoreServiceClient storeServiceClient;

    private final UserAccountService userAccountService;
    private final UserAuthService userAuthService;

    /**
     * 소셜(카카오) 로그인
     */
    @PostMapping("/login/kakao")
    public ResponseEntity<?> loginKakao(
        HttpServletRequest httpServletRequest,
        @NotEmpty(message = "token은 필수 입력값입니다.")
        @RequestParam(name = "token") String token
    ) {
        UserAccountDto userAccountDto = userAuthService.authenticateKakao(token);
        TokenInfoResponseDto tokenInfoResponse = userAuthService.login(httpServletRequest, userAccountDto);
        return ResponseEntity.ok(tokenInfoResponse);
    }

    /**
     * 소셜(카카오) 회원가입
     */
    @PostMapping("/kakao")
    public ResponseEntity<?> signUpKakao(
        @NotEmpty(message = "token은 필수 입력값입니다.")
        @RequestParam(name = "token") String token,
        @RequestBody @Valid SignUpKakaoRequestDto request
    ) {
        UserEntity user = userAccountService.signUpKakao(token, request);
        storeServiceClient.createStore(CreateStoreRequestDto.from(user, request));

        return ResponseEntity.ok().build();
    }

    /**
     * Refresh Token 을 이용한 Access Token 재 발급 (Re-Issue)
     */
    @PostMapping("/re-issue")
    public ResponseEntity<?> reIssue(
        HttpServletRequest httpServletRequest,
        @CookieValue(value = "refreshToken") String refreshToken
    ) {
        return ResponseEntity.ok(userAuthService.reIssue(httpServletRequest, refreshToken));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        userAuthService.logout(httpServletRequest);

        return ResponseEntity.ok().build();
    }
}
