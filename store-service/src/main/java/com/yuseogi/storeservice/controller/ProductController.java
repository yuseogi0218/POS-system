package com.yuseogi.storeservice.controller;

import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.dto.request.CreateProductRequestDto;
import com.yuseogi.storeservice.dto.request.UpdateProductRequestDto;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import com.yuseogi.storeservice.service.ProductService;
import com.yuseogi.storeservice.service.StoreService;
import com.yuseogi.storeservice.service.TradeDeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/store/product")
@RestController
public class ProductController {

    private final StoreService storeService;
    private final ProductService productService;
    private final TradeDeviceService tradeDeviceService;

    @PostMapping("")
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequestDto request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StoreEntity store = storeService.getStore(user.getUsername());
        productService.createProduct(store, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<?> getProductList(@CookieValue(value = "tradeDeviceId", required = false) Long tradeDeviceId) {

        StoreEntity store;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            if (tradeDeviceId == null) {
                throw new CustomException(CommonErrorCode.INSUFFICIENT_AUTHENTICATION);
            }
            TradeDeviceEntity tradeDevice = tradeDeviceService.getTradeDevice(tradeDeviceId);

            store = tradeDevice.getStore();
        } else {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            store = storeService.getStore(user.getUsername());
        }

        return ResponseEntity.ok(productService.getProductList(store));
    }

    @PatchMapping("/{product-id}")
    public ResponseEntity<?> updateProduct(
        @PathVariable("product-id") Long productId,
        @RequestBody @Valid UpdateProductRequestDto request
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StoreEntity store = storeService.getStore(user.getUsername());

        productService.updateProduct(store, productId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<?> softDeleteProduct(
        @PathVariable("product-id") Long productId
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StoreEntity store = storeService.getStore(user.getUsername());

        productService.softDeleteProduct(store, productId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/re-stock")
    public ResponseEntity<?> reStock() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StoreEntity store = storeService.getStore(user.getUsername());

        productService.reStock(store);

        return ResponseEntity.ok().build();
    }
}
