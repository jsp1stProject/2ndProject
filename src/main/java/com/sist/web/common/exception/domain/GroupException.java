package com.sist.web.common.exception.domain;

import com.sist.web.common.exception.base.BaseCustomException;
import com.sist.web.common.exception.code.GroupErrorCode;

public class GroupException extends BaseCustomException {
	private static final long serialVersionUID = 1L;
	
	public GroupException(GroupErrorCode errorCode) {
		super(errorCode);
	}
}
