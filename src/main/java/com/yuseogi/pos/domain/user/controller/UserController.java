package com.yuseogi.pos.domain.user.controller;

import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.pos.domain.user.service.UserAccountService;
import com.yuseogi.pos.domain.user.service.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserAccountService userAccountService;
    private final UserAuthService userAuthService;

    /**
     * 카카오 로그인
     */
    @PostMapping("/login/kakao")
    public ResponseEntity<?> loginKakao(
        HttpServletRequest httpServletRequest,
        @RequestParam(name = "token") String token
    ) {
        Authentication authentication = userAuthService.authenticateKakao(token);
        TokenInfoResponseDto tokenInfoResponse = userAuthService.login(httpServletRequest, authentication);
        return ResponseEntity.ok(tokenInfoResponse);
    }

    /**
     * 카카오 회원가입
     */
    @PostMapping("/kakao")
    public ResponseEntity<?> signUpKakao(
        @RequestParam(name = "token") String token,
        @RequestBody @Valid SignUpKakaoRequestDto request
    ) {
        userAccountService.signUpKakao(token, request);
        return ResponseEntity.ok().build();
    }

    /**
     * AccessToken 재 발급 (Re-Issue)
     */
    @PostMapping("/re-issue")
    public ResponseEntity<?> reIssue(
        HttpServletRequest httpServletRequest,
        @CookieValue(value = "refreshToken") String refreshToken
    ) {
        return ResponseEntity.ok(userAuthService.reIssue(httpServletRequest, refreshToken));
    }
}
