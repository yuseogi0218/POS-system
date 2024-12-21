package com.yuseogi.pos.domain.user.exception;

import com.yuseogi.pos.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    // 400 BAD_REQUEST 잘못된 요청
    EMAIL_ALREADY_USED("USER_400_01", HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일 입니다."),

    // 403 FORBIDDEN 인증 필요
    DENIED_ACCESS("USER_403_01", HttpStatus.FORBIDDEN, "해당 요청에 대한 접근 권한이 없습니다."),

    // 404 NOT_FOUND 리소스가 존재하지 않음
    NOT_FOUND_USER("USER_404_01", HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
