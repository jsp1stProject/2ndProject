package com.sist.web.test.service;

import com.sist.web.test.vo.TestVO;

public interface TestService {
	public TestVO GetName();
	public void testInsert(String pwd);
}
