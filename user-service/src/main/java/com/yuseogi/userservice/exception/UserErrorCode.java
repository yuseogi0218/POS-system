package com.yuseogi.userservice.exception;

import com.yuseogi.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    EMAIL_ALREADY_USED("USER_400_01", HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일 입니다."),

    // 401 UNAUTHORIZED 인증 자격 정보가 유효하지 않음
    INVALID_REFRESH_TOKEN("USER_401_07", HttpStatus.UNAUTHORIZED, "Refresh Token 정보가 유효하지 않습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_USER("USER_404_01", HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
