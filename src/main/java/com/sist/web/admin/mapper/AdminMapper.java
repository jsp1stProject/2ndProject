package com.sist.web.admin.mapper;

import com.sist.web.mypage.vo.SitterDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Select("SELECT USER_NO,USER_MAIL,USER_NAME,NICKNAME,num " +
            "from (select u.USER_NO,USER_MAIL,USER_NAME,NICKNAME,rownum as num " +
            "from P_USERS u join P_AUTHORITIES a on u.USER_NO=a.USER_NO where a.AUTHORITY='ROLE_SITTER' and ENABLED=1) " +
            "where num between #{start} and #{end}")
    public List<SitterDTO> getAllSitters(@Param("start")int start, @Param("end")int end);
}
