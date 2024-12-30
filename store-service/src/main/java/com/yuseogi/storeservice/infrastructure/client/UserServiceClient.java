package com.yuseogi.storeservice.infrastructure.client;

import com.yuseogi.storeservice.dto.UserAccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="user-service")
public interface UserServiceClient {

    @GetMapping("/user")
    UserAccountDto getUserAccount(@RequestParam(name = "userEmail") String userEmail);
}
