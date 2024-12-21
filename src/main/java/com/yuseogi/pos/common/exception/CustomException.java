package com.yuseogi.pos.common.exception;

import com.yuseogi.pos.common.exception.dto.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CustomException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String errorCode;
    public CustomException(ErrorCode errorcode) {
        super(errorcode.getMessage());
        this.httpStatus = errorcode.getHttpStatus();
        this.errorCode = errorcode.getCode();
    }
    public CustomException(ErrorCode errorcode, String parameter) {
        super(String.format(errorcode.getMessage(), parameter));
        this.httpStatus = errorcode.getHttpStatus();
        this.errorCode = errorcode.getCode();
    }
    public ResponseEntity<ErrorResponse> toErrorResponse() {
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(this.errorCode)
                .message(this.getMessage())
                .build();
        return ResponseEntity.status(this.httpStatus).body(response);
    }
}