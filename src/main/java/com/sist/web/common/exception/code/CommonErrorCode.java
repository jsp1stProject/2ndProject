package com.sist.web.common.exception.code;

import org.springframework.http.HttpStatus;
import com.sist.web.common.exception.base.ErrorInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorInfo {
	/** 잘못된 입력입니다. */
	INVALID_INPUT("C400", "잘못된 입력입니다.", HttpStatus.BAD_REQUEST),
	/** 로그인이 필요합니다. */
    SC_UNAUTHORIZED("C401", "로그인이 필요합니다.", HttpStatus.BAD_REQUEST),
    /** 잘못된 요청입니다. */
	INVALID_PARAMETER("C400", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
	/** 필수 요청 파라미터가 누락되었습니다 */
    MISSING_PARAMETER("C400", "필수 요청 파라미터가 누락되었습니다.", HttpStatus.BAD_REQUEST),
    /** 접근 권한이 없습니다 */
    FORBIDDEN("C403", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    /** 요청한 리소스를 찾을 수 없습니다 */
    NOT_FOUND("C404", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    /** 허용되지 않은 HTTP 메서드입니다. */
    METHOD_NOT_ALLOWED("C405", "허용되지 않은 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    /** 지원되지 않은 미디어 타입입니다. */
    UNSUPPORTED_MEDIA_TYPE("C415", "지원하지 않는 미디어 타입입니다.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    /** 서버 내부 오류가 발생했습니다. */
    INTERNAL_SERVER_ERROR("C500", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;


    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }

}
