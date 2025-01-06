package com.yuseogi.gatewayservice.infrastructure;

import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.common.exception.dto.response.ErrorResponse;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class InfrastructureServiceFilter extends AbstractGatewayFilterFactory<InfrastructureServiceFilter.Config> {

    public InfrastructureServiceFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpResponse response = exchange.getResponse();

            ResponseEntity<ErrorResponse> errorResponse = new CustomException(CommonErrorCode.DENIED_ACCESS).toErrorResponse();

            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.setStatusCode(errorResponse.getStatusCode());

            String responseBody = String.format("{\"errorCode\": \"%s\", \"message\": \"%s\"}", errorResponse.getBody().errorCode(), errorResponse.getBody().message());

            DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes());
            return response.writeWith(Mono.just(buffer));
        });
    }

    public static class Config { }
}
