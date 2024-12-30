package com.yuseogi.storeservice.dto;

public record UserAccountDto(
    Long id,
    String email,
    String role
) {}
