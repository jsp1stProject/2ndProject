package com.sist.web.common.exception.code;

import org.springframework.http.HttpStatus;
import com.sist.web.common.exception.base.ErrorInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GroupErrorCode implements ErrorInfo {
	/** 그룹이 존재하지 않습니다. */
	GROUP_NOT_FOUND("G404", "그룹이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	/** 이미 존재하는 그룹입니다. */
    DUPLICATE_GROUP("G409", "이미 존재하는 그룹입니다.", HttpStatus.CONFLICT),
    /** 그룹에 추가할 사용자를 찾을 수 없습니다. */
	USER_NOT_FOUND("G404", "그룹에 추가할 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	/** 이미지 업로드에 실패했습니다. */
	IMAGE_UPLOAD_FAILED("G500", "이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	/** 그룹에 다른 멤버가 존재하여 삭제할 수 없습니다. */
	CANNOT_DELETE_GROUP_WITH_MULTIPLE_MEMBERS("G403", "그룹에 다른 멤버가 존재하여 삭제할 수 없습니다.", HttpStatus.FORBIDDEN),
	/** 해당 기능은 그룹장만 할 수 있습니다. */
	NOT_GROUP_OWNER("G403", "해당 기능은 그룹장만 할 수 있습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;


    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }
}
