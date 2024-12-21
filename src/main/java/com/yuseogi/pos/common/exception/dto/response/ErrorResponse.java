package com.yuseogi.pos.common.exception.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
    String errorCode,
    String message
) { }