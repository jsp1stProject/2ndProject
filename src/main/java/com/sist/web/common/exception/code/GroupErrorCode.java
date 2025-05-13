package com.sist.web.common.exception.code;

import org.springframework.http.HttpStatus;
import com.sist.web.common.exception.base.ErrorInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GroupErrorCode implements ErrorInfo {
	GROUP_NOT_FOUND("G404", "그룹이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_GROUP("G409", "이미 존재하는 그룹입니다.", HttpStatus.CONFLICT),
	USER_NOT_FOUND("G007", "그룹에 추가할 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;


    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }
}
