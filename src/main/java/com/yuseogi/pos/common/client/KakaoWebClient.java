package com.yuseogi.pos.common.client;

import com.yuseogi.pos.common.client.dto.response.KakaoAccountResponseDto;
import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.common.exception.CustomException;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KakaoWebClient {

    @Value("${kakao.account-uri}")
    private String kakaoAccountUri;

    public KakaoAccountResponseDto getAccount(String kakaoAccessToken) {
        return WebClient.create(kakaoAccountUri)
            .get()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken) // access token 인가
            .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
            .retrieve()
            .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR)))
            .bodyToMono(KakaoAccountResponseDto.class)
            .block();
    }
}
