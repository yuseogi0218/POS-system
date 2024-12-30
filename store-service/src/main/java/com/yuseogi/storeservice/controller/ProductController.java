package com.yuseogi.storeservice.controller;

import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.common.util.ParseRequestUtil;
import com.yuseogi.storeservice.dto.request.CreateProductRequestDto;
import com.yuseogi.storeservice.dto.request.UpdateProductRequestDto;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import com.yuseogi.storeservice.service.ProductService;
import com.yuseogi.storeservice.service.StoreService;
import com.yuseogi.storeservice.service.TradeDeviceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/store/product")
@RestController
public class ProductController {

    private final StoreService storeService;
    private final ProductService productService;
    private final TradeDeviceService tradeDeviceService;

    @PostMapping("")
    public ResponseEntity<?> createProduct(
        HttpServletRequest httpServletRequest,
        @RequestBody @Valid CreateProductRequestDto request
    ) {

        String userEmail = ParseRequestUtil.extractUserEmailFromRequest(httpServletRequest);

        StoreEntity store = storeService.getStore(userEmail);
        productService.createProduct(store, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<?> getProductList(
        HttpServletRequest httpServletRequest,
        @CookieValue(value = "tradeDeviceId", required = false) Long tradeDeviceId
    ) {

        StoreEntity store;

        String userEmail = ParseRequestUtil.extractUserEmailFromRequest(httpServletRequest);
        if (StringUtils.isEmpty(userEmail)) {
            if (tradeDeviceId == null) {
                throw new CustomException(CommonErrorCode.INSUFFICIENT_AUTHENTICATION);
            }
            TradeDeviceEntity tradeDevice = tradeDeviceService.getTradeDevice(tradeDeviceId);

            store = tradeDevice.getStore();
        } else {
            store = storeService.getStore(userEmail);
        }

        return ResponseEntity.ok(productService.getProductList(store));
    }

    @PatchMapping("/{product-id}")
    public ResponseEntity<?> updateProduct(
        HttpServletRequest httpServletRequest,
        @PathVariable("product-id") Long productId,
        @RequestBody @Valid UpdateProductRequestDto request
    ) {
        String userEmail = ParseRequestUtil.extractUserEmailFromRequest(httpServletRequest);

        StoreEntity store = storeService.getStore(userEmail);

        productService.updateProduct(store, productId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<?> softDeleteProduct(
        HttpServletRequest httpServletRequest,
        @PathVariable("product-id") Long productId
    ) {
        String userEmail = ParseRequestUtil.extractUserEmailFromRequest(httpServletRequest);

        StoreEntity store = storeService.getStore(userEmail);

        productService.softDeleteProduct(store, productId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/re-stock")
    public ResponseEntity<?> reStock(HttpServletRequest httpServletRequest) {
        String userEmail = ParseRequestUtil.extractUserEmailFromRequest(httpServletRequest);

        StoreEntity store = storeService.getStore(userEmail);

        productService.reStock(store);

        return ResponseEntity.ok().build();
    }
}
