package com.sist.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.sist.vo.TestVO;

public interface TestMapper {
	@Select("SELECT name FROM USER_INFO WHERE USER_ID='shim11'")
	public TestVO GetName();

	@Insert("insert into user_info(user_id,pwd,name,nickname,birthday,email,post,addr1,phone,admin) values('testuser2',#{pwd},'testuser','tes132322t',sysdate,'test@test324.com',03132,'dd','010-0000-0000','N')")
	public void testInsert(String pwd);
}
