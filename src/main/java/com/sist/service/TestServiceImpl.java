package com.sist.service;

import org.springframework.stereotype.Service;

import com.sist.mapper.TestMapper;
import com.sist.vo.TestVO;

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
