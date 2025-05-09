package com.sist.web.common.exception.domain;

import org.springframework.http.HttpStatus;

import com.sist.web.common.exception.base.BaseCustomException;
import com.sist.web.common.exception.base.ErrorInfo;

public class GroupChatException extends BaseCustomException {

	private static final long serialVersionUID = 1L;

	public GroupChatException(ErrorInfo errorType) {
		super(errorType);
	}
	
	public GroupChatException(ErrorInfo errorType, String customMessage) {
        super(new ErrorInfo() {
            @Override public String getCode() { return errorType.getCode(); }
            @Override public HttpStatus getStatus() { return errorType.getStatus(); }
            @Override public String getMessage() { return customMessage; }
        });
    }
    
}
