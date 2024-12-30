package com.yuseogi.userservice.infrastructure.client;

import com.yuseogi.userservice.infrastructure.client.dto.response.KakaoAccountResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-web-client", url = "${kakao.account-uri}")
public interface KakaoWebClient {

    @GetMapping(consumes = "application/x-www-form-urlencoded")
    KakaoAccountResponseDto getAccount(@RequestHeader("Authorization") String kakaoAccessToken);

}
