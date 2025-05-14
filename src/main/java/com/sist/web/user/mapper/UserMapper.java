package com.sist.web.user.mapper;

import com.sist.web.user.vo.UserDetailDTO;
import com.sist.web.user.vo.UserVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public interface UserMapper {
    @Select("SELECT USER_NO from P_USERS where user_mail=#{user_mail} and ENABLED=1")
    public String getEnableUserNo(String user_mail);

    @Select("SELECT ENABLED from P_USERS where USER_NO=#{userno}")
    public int checkUserActive(String userno);

    @Select("SELECT u.USER_NO,user_mail,SOCIAL_ID,PASSWORD,USER_NAME,NICKNAME,PHONE,TO_CHAR(BIRTHDAY,'YYYY-MM-DD') as DB_BIRTHDAY,ENABLED,PROFILE,a.AUTHORITY FROM P_USERS u join P_AUTHORITIES a on u.user_no=a.user_no WHERE u.USER_NO=#{user_no}")
    public UserDetailDTO getUserDtoFromUserNo(String user_no);

    @Select("SELECT P_USER_NO_SEQ.nextval from dual")
    public Long getNextUserNo();

    @Insert("insert into p_users(user_no,user_mail,password,user_name,nickname) " +
            "values(#{user_no},#{user_mail},#{password},#{user_name},#{nickname})")
    public void insertDefaultUser(UserVO vo);

    @Insert("insert into p_users(user_no,user_mail,user_name,nickname,SOCIAL_ID,profile) " +
            "values(#{user_no},#{user_mail},#{user_name},#{nickname},#{social_id},#{profile})")
    public void insertKakaoUser(UserVO vo);

    @Insert("insert into P_AUTHORITIES(user_no, AUTHORITY) values(#{user_no},#{authority})")
    public void insertUserAuthority(Map<String, Object> map);

    @Select("SELECT count(*) from P_USERS where user_mail=#{user_mail} and ENABLED=1")
    public int getUserMailCount(String user_mail);

    @Select("SELECT password from P_USERS where user_mail=#{user_mail} and ENABLED=1")
    public String getPassword(String user_mail);

    @Delete("DELETE FROM P_AUTHORITIES where user_no=#{user_no}")
    public void deleteUserAuthority(String user_mail);

    @Update("UPDATE p_users set ENABLED=0, USER_MAIL='', SOCIAL_ID='', PASSWORD='', USER_NAME='', NICKNAME='탈퇴한 회원', PHONE='', BIRTHDAY='' where USER_MAIL=#{user_mail} and ENABLED=1")
    public void enableUser(String user_mail);

    @Select("Select USER_NO, USER_MAIL, NICKNAME FROM P_USERS where USER_NO=#{user_no}")
    public UserVO getUserMailFromUserNo(String user_no);

    @Update("UPDATE P_USERS set USER_MAIL=#{user_mail}, USER_NAME=#{user_name}, NICKNAME=#{nickname}, PASSWORD=#{password,jdbcType=VARCHAR}, BIRTHDAY=#{birthday,jdbcType=DATE}, PHONE=#{phone,jdbcType=VARCHAR}, ADDR=#{addr,jdbcType=VARCHAR} where USER_NO=#{user_no}")
    public void updateUser(UserVO vo);

//  카카오 로그인 시
//  소셜연동된 계정인지 확인
    @Select("select SOCIAL_ID from P_USERS where USER_MAIL=#{user_mail}")
    public String getSocialId(String user_mail);

//  소셜연동됐다면 로그인
    @Select("select u.USER_NO, USER_MAIL, a.AUTHORITY, NICKNAME from P_USERS u join P_AUTHORITIES a on u.user_no=a.user_no where SOCIAL_ID=#{social_id}")
    public UserVO getUserVOFromSocialId(String user_mail);
}
