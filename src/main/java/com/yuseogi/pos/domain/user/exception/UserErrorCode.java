package com.yuseogi.pos.domain.user.exception;

import com.yuseogi.pos.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_USER("USER_404_01", HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
