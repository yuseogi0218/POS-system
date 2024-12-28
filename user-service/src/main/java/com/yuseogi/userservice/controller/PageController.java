package com.yuseogi.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class PageController {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @GetMapping("/page/login")
    public String login(Model model) {
        model.addAttribute("kakaoClientId", kakaoClientId);
        model.addAttribute("kakaoRedirectUri", kakaoRedirectUri);

        return "login";
    }

    @GetMapping("/page/signup")
    public String signUp(
            @RequestParam(name = "oauth", required = false) String oauth,
            @RequestParam(name = "code", required = false) String code,
            Model model
    ) {
        model.addAttribute("oauth", oauth);
        model.addAttribute("clientId", kakaoClientId);
        model.addAttribute("code", code);
        return "signup";
    }
}
