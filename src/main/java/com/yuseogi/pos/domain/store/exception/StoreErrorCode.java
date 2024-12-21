package com.yuseogi.pos.domain.store.exception;

import com.yuseogi.pos.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StoreErrorCode implements ErrorCode {

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_STORE("STORE_404_01", HttpStatus.NOT_FOUND, "존재하지 않는 상점 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
