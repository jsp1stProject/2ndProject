package com.sist.web.admin.mapper;

import com.sist.web.mypage.vo.SitterDTO;
import com.sist.web.user.vo.UserDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    @Select("SELECT USER_NO,USER_MAIL,USER_NAME,NICKNAME,num " +
            "from (select u.USER_NO,USER_MAIL,USER_NAME,NICKNAME,rownum as num " +
            "from P_USERS u join P_AUTHORITIES a on u.USER_NO=a.USER_NO where a.AUTHORITY='ROLE_SITTER' and ENABLED=1) " +
            "where num between #{start} and #{end}")
    public List<SitterDTO> getAllSitters(@Param("start")int start, @Param("end")int end);

    @Select("SELECT CEIL(COUNT(*)/#{rowsize}) FROM P_USERS u join P_AUTHORITIES a on u.USER_NO=a.USER_NO where a.AUTHORITY='ROLE_SITTER' and ENABLED=1")
    public int countSitters();

    public List<UserDetailDTO> getUsers(Map<String, Object> map);
    public int getUsersTotalPage(Map<String, Object> map);
    public List<SitterDTO> getSitterApps(Map<String, Object> map);
    public int getSitterAppsTotalPage(Map<String, Object> map);
}
