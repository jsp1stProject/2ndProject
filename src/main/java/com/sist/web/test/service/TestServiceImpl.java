package com.sist.web.test.service;

import org.springframework.stereotype.Service;

import com.sist.web.test.mapper.TestMapper;
import com.sist.web.test.vo.TestVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
	private final TestMapper testMapper;

	@Override
	public TestVO GetName() {
		// TODO Auto-generated method stub
		return testMapper.GetName();
	}
	
}
