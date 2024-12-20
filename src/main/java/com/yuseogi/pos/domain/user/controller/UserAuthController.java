package com.yuseogi.pos.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserAuthController {

    /**
     * 카카오 로그인
     */
    @PostMapping("/login/kakao")
    public ResponseEntity<?> loginKakao(@RequestParam(name = "token") String token) {
        return ResponseEntity.ok().build();
    }
}
