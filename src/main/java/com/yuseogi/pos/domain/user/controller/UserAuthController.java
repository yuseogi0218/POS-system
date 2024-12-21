package com.yuseogi.pos.domain.user.controller;

import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import com.yuseogi.pos.domain.user.controller.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.pos.domain.user.controller.dto.response.LoginKakaoResponseDto;
import com.yuseogi.pos.domain.user.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserAuthController {

    private final UserAuthService userAuthService;

    /**
     * 카카오 로그인
     */
    @PostMapping("/login/kakao")
    public ResponseEntity<?> loginKakao(@RequestParam(name = "token") String token) {
        Authentication authentication = userAuthService.authenticateKakao(token);
        TokenInfoResponseDto tokenInfoResponse = userAuthService.login(authentication);
        return ResponseEntity.ok(LoginKakaoResponseDto.fromTokenInfoResponse(tokenInfoResponse));
    }

    /**
     * 카카오 회원가입
     */
    @PostMapping("/kakao")
    public ResponseEntity<?> signUpKakao(
        @RequestParam(name = "token") String token,
        @RequestBody @Valid SignUpKakaoRequestDto request
    ) {
        return ResponseEntity.ok().build();
    }
}
