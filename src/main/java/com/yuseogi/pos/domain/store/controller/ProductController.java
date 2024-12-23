package com.yuseogi.pos.domain.store.controller;

import com.yuseogi.pos.domain.store.dto.request.CreateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.request.UpdateProductRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/store/product")
public class ProductController {

    @PostMapping("")
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequestDto request) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<?> getProductList(@CookieValue(value = "tradeDeviceId", required = false) Long tradeDeviceId) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
        @PathVariable("productId") Long productId,
        @RequestBody @Valid UpdateProductRequestDto request
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> softDeleteProduct(
        @PathVariable("productId") Long productId
    ) {
        return ResponseEntity.ok().build();
    }
}
