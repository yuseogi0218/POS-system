package com.yuseogi.storeservice.infrastructure.server;

import com.yuseogi.storeservice.dto.ProductInfoDto;
import com.yuseogi.storeservice.dto.TradeDeviceInfoDto;
import com.yuseogi.storeservice.dto.request.CreateStoreRequestDto;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.service.ProductService;
import com.yuseogi.storeservice.service.StoreService;
import com.yuseogi.storeservice.service.TradeDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/store")
@RestController
public class StoreServiceInfraController {

    private final StoreService storeService;
    private final ProductService productService;
    private final TradeDeviceService tradeDeviceService;

    @PostMapping("")
    public ResponseEntity<?> createStore(@RequestBody CreateStoreRequestDto request) {
        storeService.createStore(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/product/{product-id}")
    public ResponseEntity<?> getProductInfo(@PathVariable("product-id") Long productId) {
        return ResponseEntity.ok(new ProductInfoDto(productService.getProduct(productId)));
    }

    @GetMapping("/trade-device/check-authority/{trade-device-id}")
    public ResponseEntity<?> checkAuthorityTradeDevice(
        @PathVariable("trade-device-id") Long tradeDeviceId,
        @RequestParam(value = "user-id") Long userId
    ) {
        StoreEntity store = storeService.getStoreByOwnerUser(userId);
        tradeDeviceService.checkAuthorityTradeDevice(store, tradeDeviceId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/trade-device/{trade-device-id}")
    public ResponseEntity<?> getTradeDevice(@PathVariable("trade-device-id") Long tradeDeviceId) {
        return ResponseEntity.ok(new TradeDeviceInfoDto(tradeDeviceService.getTradeDevice(tradeDeviceId)));
    }

}
