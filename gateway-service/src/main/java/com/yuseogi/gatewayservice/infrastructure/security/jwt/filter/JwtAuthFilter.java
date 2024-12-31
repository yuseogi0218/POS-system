package com.yuseogi.gatewayservice.infrastructure.security.jwt.filter;

import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.common.exception.dto.response.ErrorResponse;
import com.yuseogi.gatewayservice.infrastructure.security.jwt.component.JwtProvider;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final JwtProvider jwtProvider;

    public JwtAuthFilter(JwtProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            HttpMethod httpMethod = request.getMethod();
            String uri = request.getURI().getPath();

            try {

                String token = jwtProvider.resolveToken(request);

                if (token == null) {
                    if ((httpMethod.equals(HttpMethod.GET) && uri.equals("/store/product"))) {
                        return chain.filter(exchange);
                    }
                    throw new CustomException(CommonErrorCode.INSUFFICIENT_AUTHENTICATION);
                }

                if (jwtProvider.validateToken(token)) {
                    jwtProvider.validateTokenType(token, JwtProvider.TYPE_ACCESS);

                    String userId = jwtProvider.getUserId(token);

                    ServerHttpRequest.Builder mutatedRequest = request.mutate();
                    mutatedRequest.header("X-Authorization-userId", userId);

                    if (httpMethod.equals(HttpMethod.POST) && uri.equals("/user/logout")) {
                        mutatedRequest.header("X-Authorization-accessToken", token);
                    }

                    request = mutatedRequest.build();
                    exchange = exchange.mutate().request(request).build();
                }

            } catch (CustomException e) {
                return handleJwtException(response, e);
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> handleJwtException(ServerHttpResponse response, CustomException e) {
        ResponseEntity<ErrorResponse> errorResponse = e.toErrorResponse();

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(errorResponse.getStatusCode());

        String responseBody = String.format("{\"errorCode\": \"%s\", \"message\": \"%s\"}", errorResponse.getBody().errorCode(), errorResponse.getBody().message());

        DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config { }
}
