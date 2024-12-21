package com.yuseogi.pos.domain.user.controller;

import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import com.yuseogi.pos.domain.user.controller.dto.response.LoginKakaoResponseDto;
import com.yuseogi.pos.domain.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
