package com.sist.web.common.exception.code;

import org.springframework.http.HttpStatus;

import com.sist.web.common.exception.base.ErrorInfo;

public enum CommonErrorCode implements ErrorInfo {

	INVALID_INPUT("C001", "잘못된 입력입니다.", HttpStatus.BAD_REQUEST),
	FORBIDDEN("C403", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    INTERNAL_ERROR("C999", "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    CommonErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }

}
