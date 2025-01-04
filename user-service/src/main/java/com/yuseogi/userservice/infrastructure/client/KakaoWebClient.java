package com.yuseogi.userservice.infrastructure.client;

import com.yuseogi.userservice.infrastructure.client.dto.response.KakaoAccountResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@CircuitBreaker(name = "circuit-breaker")
@FeignClient(name = "kakao-web-client", url = "${kakao.account-uri}")
public interface KakaoWebClient {

    @GetMapping(consumes = "application/x-www-form-urlencoded")
    KakaoAccountResponseDto getAccount(@RequestHeader("Authorization") String kakaoAccessToken);

}
