package com.sist.mapper;

import org.apache.ibatis.annotations.Select;

import com.sist.vo.TestVO;

public interface TestMapper {
	@Select("SELECT name FROM USER_INFO WHERE USER_ID='shim11'")
	public TestVO GetName();
}
