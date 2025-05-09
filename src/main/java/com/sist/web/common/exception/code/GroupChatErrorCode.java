package com.sist.web.common.exception.code;

import org.springframework.http.HttpStatus;
import com.sist.web.common.exception.base.ErrorInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GroupChatErrorCode implements ErrorInfo {
	INVALID_JOIN_REQUEST("G400", "잘못된 접속 요청입니다", HttpStatus.BAD_REQUEST),
	UNAUTHORIZED("G401", "인증되지 않은 WebSocket 연결입니다", HttpStatus.UNAUTHORIZED);
	
	private final String code;
    private final String message;
    private final HttpStatus status;


    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }
}
