package com.yuseogi.tradeservice.infrastructure.client;

import com.yuseogi.tradeservice.dto.ProductInfoDto;
import com.yuseogi.tradeservice.dto.TradeDeviceInfoDto;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.request.DecreaseProductStockRequestMessage;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@CircuitBreaker(name = "circuit-breaker")
@FeignClient(name = "store-service")
public interface StoreServiceClient {

    @GetMapping("/infra/product/{product-id}")
    ProductInfoDto getProductInfo(@PathVariable("product-id") Long productId);

    @GetMapping("/infra/trade-device/check-authority/{trade-device-id}")
    void checkAuthorityTradeDevice(@PathVariable("trade-device-id") Long tradeDeviceId, @RequestParam("user-id") Long userId);

    @GetMapping("/infra/trade-device/{trade-device-id}")
    TradeDeviceInfoDto getTradeDevice(@PathVariable("trade-device-id") Long tradeDeviceId);

}
