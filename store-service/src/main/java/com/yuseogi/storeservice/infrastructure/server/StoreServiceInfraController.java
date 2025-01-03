package com.yuseogi.storeservice.infrastructure.server;

import com.yuseogi.storeservice.dto.ProductInfoDto;
import com.yuseogi.storeservice.dto.TradeDeviceInfoDto;
import com.yuseogi.storeservice.dto.request.CreateStoreRequestDto;
import com.yuseogi.storeservice.dto.request.DecreaseProductStockRequestDto;
import com.yuseogi.storeservice.entity.ProductEntity;
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

    @PatchMapping("/product/stock/{product-id}")
    public ResponseEntity<?> decreaseStock(
        @PathVariable("product-id") Long productId,
        @RequestBody DecreaseProductStockRequestDto request
    ) {
        StoreEntity store = storeService.getStore(request.storeId());

        ProductEntity product = productService.decreaseStock(store, productId, request.decreasingStock());

        return ResponseEntity.ok(new ProductInfoDto(product));
    }

    @GetMapping("/trade-device/check-exist/{trade-device-id}")
    public ResponseEntity<?> checkExistTradeDevice(@PathVariable("trade-device-id") Long tradeDeviceId) {
        tradeDeviceService.checkExistTradeDevice(tradeDeviceId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/trade-device/{trade-device-id}")
    public ResponseEntity<?> getTradeDevice(@PathVariable("trade-device-id") Long tradeDeviceId) {
        return ResponseEntity.ok(new TradeDeviceInfoDto(tradeDeviceService.getTradeDevice(tradeDeviceId)));
    }

}
