package com.sist.web.user.mapper;

import com.sist.web.user.vo.UserVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public interface UserMapper {
    @Select("SELECT USER_NO from P_USERS where user_mail=#{user_mail} and ENABLED=1")
    public String getEnableUserNo(String user_mail);

    @Insert("insert into p_users(user_no,user_mail,password,user_name,nickname) " +
            "values(p_user_no_seq.nextval,#{user_mail},#{password},#{user_name},#{nickname})")
    public void insertDefaultUser(UserVO vo);

    @Insert("insert into p_users(user_no,user_mail,user_name,nickname,SOCIAL_ID) " +
            "values(p_user_no_seq.nextval,#{user_mail},#{user_name},#{nickname},#{social_id})")
    public void insertKakaoUser(UserVO vo);

    @Insert("insert into P_AUTHORITIES values(#{user_mail},#{authority})")
    public void insertUserAuthority(Map<String, Object> map);

    @Select("SELECT count(*) from P_USERS where user_mail=#{user_mail} and ENABLED=1")
    public int getUserMailCount(String user_mail);

    @Select("SELECT password from P_USERS where user_mail=#{user_mail} and ENABLED=1")
    public String getPassword(String user_mail);

    @Delete("DELETE FROM P_AUTHORITIES where user_mail=#{user_mail}")
    public void deleteUserAuthority(String user_mail);

    @Update("UPDATE p_users set ENABLED=0, USER_MAIL='', SOCIAL_ID='', PASSWORD='', USER_NAME='', NICKNAME='탈퇴한 회원', PHONE='', BIRTHDAY='' where USER_MAIL=#{user_mail} and ENABLED=1")
    public void enableUser(String user_mail);
}
