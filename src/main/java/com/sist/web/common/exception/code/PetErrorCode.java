package com.sist.web.common.exception.code;

import com.sist.web.common.exception.base.ErrorInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PetErrorCode implements ErrorInfo {
    NO_PROFILE("P400","반려동물의 사진을 첨부해주세요.", HttpStatus.BAD_REQUEST),
    NO_TYPE("P400","반려동물의 종을 선택해주세요.", HttpStatus.BAD_REQUEST),
    CONFLICT_APPLY("P409","이미 펫시터 권한이 있습니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;


    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }
}
