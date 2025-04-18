package com.sist.web.user.mapper;

import com.sist.web.user.vo.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface UserMapper {
    @Insert("insert into p_users(user_no,user_mail,password,user_name,nickname) " +
            "values(p_user_no_seq.nextval,#{user_mail},#{password},#{user_name},#{nickname})")
    public void insertDefaultUser(UserVO vo);

    @Insert("insert into P_AUTHORITIES values(#{user_mail},#{authority})")
    public void insertUserAuthority(Map<String, Object> map);

    @Select("SELECT count(*) from P_USERS where user_mail=#{user_mail}")
    public int getUserMailCount(String user_mail);

    @Select("SELECT password from P_USERS where user_mail=#{user_mail}")
    public String getPassword(String user_mail);
}
