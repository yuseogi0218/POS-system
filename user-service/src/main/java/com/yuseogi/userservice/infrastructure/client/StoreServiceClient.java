package com.yuseogi.userservice.infrastructure.client;

import com.yuseogi.userservice.dto.request.CreateStoreRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "circuit-breaker")
@FeignClient(name="store-service")
public interface StoreServiceClient {

    @PostMapping("/infra")
    void createStore(@RequestBody CreateStoreRequestDto request);
}
