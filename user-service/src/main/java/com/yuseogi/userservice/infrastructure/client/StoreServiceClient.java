package com.yuseogi.userservice.infrastructure.client;

import com.yuseogi.userservice.dto.request.CreateStoreRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="store-service")
public interface StoreServiceClient {

    @PostMapping("/store")
    void createStore(@RequestBody CreateStoreRequestDto request);
}
