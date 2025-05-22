package com.sist.web.admin.mapper;

import com.sist.web.admin.dto.AdminSitterAppDetailDTO;
import com.sist.web.admin.dto.AdminSitterListDTO;
import com.sist.web.admin.dto.AdminUserGroupDTO;
import com.sist.web.mypage.vo.PetDTO;
import com.sist.web.mypage.vo.SitterDTO;
import com.sist.web.admin.dto.AdminUserDetailDTO;
import com.sist.web.user.vo.UserDetailDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface AdminMapper {
    public List<AdminUserDetailDTO> getUsers(Map<String, Object> map);
    public int getUsersTotalPage(Map<String, Object> map);
    public List<AdminSitterListDTO> getSitterApps(Map<String, Object> map);
    public int getSitterAppsTotalPage(Map<String, Object> map);

    @Select("SELECT u.USER_NO,u.USER_NAME,u.NICKNAME,u.USER_MAIL,u.SOCIAL_ID,u.PROFILE," +
            "a.AUTHORITY,TO_CHAR(u.BIRTHDAY,'YYYY.mm.DD') as db_birthday,u.ADDR,TO_CHAR(u.REGDATE,'YYYY.mm.DD') as db_regdate," +
            "s.SITTER_PIC,s.SITTER_NO,s.CARECOUNT,s.SCORE,s.TAG,s.CARE_LOC," +
            "s.PET_FIRST_PRICE,s.HISTORY,s.LICENSE,s.ACTIVE " +
            "from P_USERS u LEFT JOIN P_SITTER s on u.USER_NO = s.USER_NO " +
            "JOIN P_AUTHORITIES a ON u.USER_NO =a.USER_NO " +
            "where u.USER_NO=#{user_no}")
    public AdminUserDetailDTO getUserDetail(String user_no);


    @Select("SELECT u.USER_NO,u.USER_NAME,u.NICKNAME,u.USER_MAIL,u.PROFILE,TO_CHAR(u.BIRTHDAY,'YYYY.mm.DD') as dbbirth,u.ADDR,s.HISTORY,s.INFO,s.LICENSE,s.STATUS " +
            "from P_USERS u JOIN P_SITTER_APP s on u.USER_NO = s.USER_NO " +
            "where u.USER_NO=#{user_no}")
    public AdminSitterAppDetailDTO getSitterAppDetail(String user_no);

    @Select("SELECT PET_NAME,PET_TYPE,PET_SUBTYPE,PET_AGE,PET_WEIGHT FROM P_PETS where USER_NO=#{user_no}")
    public List<PetDTO> getPetsDetail(String user_no);

    @Select("SELECT m.GROUP_NO,g.GROUP_NAME,g.PROFILE_IMG,m.NICKNAME,TO_CHAR(m.JOINED_AT,'YYYY.mm.DD') as JOINED_AT,m.ROLE " +
            "FROM P_GROUP g JOIN P_GROUP_MEMBER m ON g.GROUP_NO = m.GROUP_NO WHERE m.USER_NO=#{user_no}")
    public List<AdminUserGroupDTO> getUserGroupList(String user_no);

    @Update("UPDATE P_SITTER_APP SET STATUS=#{status} WHERE USER_NO=#{user_no}")
    public void updateSitterAppStatus(@Param("user_no")String user_no, @Param("status")String status);

    @Insert("INSERT INTO P_SITTER (SITTER_NO,USER_NO,APP_NO,HISTORY,LICENSE,CARE_LOC,CONTENT,SITTER_PIC) " +
            "SELECT P_SIT_NO_SEQ.nextval,a.USER_NO,a.APP_NO,a.HISTORY,a.LICENSE,u.ADDR,a.INFO,u.PROFILE FROM P_SITTER_APP a JOIN P_USERS u ON a.USER_NO = u.USER_NO " +
            "WHERE a.USER_NO=#{user_no}")
    public void insertSitter(String user_no);

    @Update("UPDATE P_AUTHORITIES SET AUTHORITY=#{role} WHERE USER_NO=#{user_no}")
    public void updateAuthorities(@Param("user_no")String user_no, @Param("role")String role);
}
