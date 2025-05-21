package com.sist.web.admin.mapper;

import com.sist.web.mypage.vo.PetDTO;
import com.sist.web.mypage.vo.SitterDTO;
import com.sist.web.user.vo.UserDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface AdminMapper {
    public List<UserDetailDTO> getUsers(Map<String, Object> map);
    public int getUsersTotalPage(Map<String, Object> map);
    public List<SitterDTO> getSitterApps(Map<String, Object> map);
    public int getSitterAppsTotalPage(Map<String, Object> map);

    @Select("SELECT u.USER_NO,u.USER_NAME,u.NICKNAME,u.USER_MAIL,TO_CHAR(u.BIRTHDAY,'YYYY.mm.DD') as dbbirth,u.ADDR,s.HISTORY,s.INFO,s.LICENSE,s.STATUS " +
            "from P_USERS u JOIN P_SITTER_APP s on u.USER_NO = s.USER_NO " +
            "where u.USER_NO=#{user_no}")
    public SitterDTO getSitterAppDetail(String user_no);

    @Select("SELECT PET_NAME,PET_TYPE,PET_SUBTYPE,PET_AGE,PET_WEIGHT FROM P_PETS where USER_NO=#{user_no}")
    public List<PetDTO> getPetsDetail(String user_no);
}
