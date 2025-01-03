package com.yuseogi.tradeservice.infrastructure.client;

import com.yuseogi.tradeservice.dto.ProductInfoDto;
import com.yuseogi.tradeservice.dto.TradeDeviceInfoDto;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.request.DecreaseProductStockRequestMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "store-service")
public interface StoreServiceClient {

    @GetMapping("/store/product/{product-id}")
    ProductInfoDto getProductInfo(@PathVariable("product-id") Long productId);

    @PatchMapping("/store/product/stock/{product-id}")
    void decreaseProductStock(
        @PathVariable("product-id") Long productId,
        @RequestBody DecreaseProductStockRequestMessage request
    );

    @GetMapping("/store/trade-device/check-exist/{trade-device-id}")
    void checkExistTradeDevice(@PathVariable("trade-device-id") Long tradeDeviceId);

    @GetMapping("/store/trade-device/{trade-device-id}")
    TradeDeviceInfoDto getTradeDevice(@PathVariable("trade-device-id") Long tradeDeviceId);

}
