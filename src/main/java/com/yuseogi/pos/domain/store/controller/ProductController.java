package com.yuseogi.pos.domain.store.controller;

import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.domain.store.dto.request.CreateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.request.UpdateProductRequestDto;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.service.ProductService;
import com.yuseogi.pos.domain.store.service.StoreService;
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

            store = storeService.getStore(tradeDeviceId);
        } else {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            store = storeService.getStore(user.getUsername());
        }

        return ResponseEntity.ok(productService.getProductList(store));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
        @PathVariable("productId") Long productId,
        @RequestBody @Valid UpdateProductRequestDto request
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StoreEntity store = storeService.getStore(user.getUsername());

        productService.updateProduct(store, productId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> softDeleteProduct(
        @PathVariable("productId") Long productId
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StoreEntity store = storeService.getStore(user.getUsername());

        productService.softDeleteProduct(store, productId);

        return ResponseEntity.ok().build();
    }
}
