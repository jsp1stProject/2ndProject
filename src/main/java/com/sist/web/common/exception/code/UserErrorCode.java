package com.sist.web.common.exception.code;

import com.sist.web.common.exception.base.ErrorInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorInfo {
    FAILED_LOGIN("401", "아이디나 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND("404", "요청한 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CONFLICT_EMAIL("409", "이미 가입된 이메일입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;


    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }
}
