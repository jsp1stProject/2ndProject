package com.sist.web.mypage.mapper;

import com.sist.web.mypage.vo.PetDTO;
import com.sist.web.mypage.vo.PetVO;
import com.sist.web.mypage.vo.SitterDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MypageMapper {
    @Select("SELECT PET_NO,PET_NAME,PET_AGE,PET_TYPE,PET_SUBTYPE,PET_WEIGHT,PET_CHAR1,PET_CHAR2,pet_char3,PET_PROFILEPIC from P_PETS where USER_NO=#{userno}")
    public List<PetDTO> getMyPets(String userno);

    @Select("SELECT PET_NO,PET_NAME,PET_AGE,PET_TYPE,PET_SUBTYPE,PET_WEIGHT,PET_CHAR1,PET_CHAR2,pet_char3,PET_STATUS,pet_weight,PET_PROFILEPIC from P_PETS where USER_NO=#{userno} AND PET_NO=${petno}")
    public PetDTO getMyPetDetail(@Param("userno")String Userno, @Param("petno")String petno);

    @Select("SELECT P_PETS_NO_SEQ.nextval from dual")
    public Long getNextPetNo();

    @Insert("INSERT INTO P_PETS(pet_no, user_no, pet_name, pet_profilepic, pet_age, pet_type, pet_subtype, pet_char1, pet_char2, pet_char3, pet_status, pet_weight) " +
            "VALUES (#{pet_no},#{user_no},#{pet_name},#{pet_profilepic},#{pet_age},#{pet_type},#{pet_subtype, jdbcType=VARCHAR},#{pet_char1},#{pet_char2},#{pet_char3},#{pet_status},#{pet_weight})")
    public void insertMyPet(PetVO vo);

    @Select("select COUNT(*) from P_PETS WHERE PET_NO=#{petno} AND USER_NO=#{userno}")
    public int checkPetExists(@Param("userno")String Userno, @Param("petno")String petno);

    public void updateMyPet(PetVO vo);

    @Delete("delete from P_PETS where PET_NO=#{petno}")
    public void DeleteMyPet(String petno);

    @Insert("INSERT INTO P_SITTER_APP(app_no, pet_no, user_no, history, license, info) " +
            "VALUES (APP_NO_SEQ.nextval,#{pet_no, jdbcType=DOUBLE},#{user_no},#{history},#{license},#{info})")
    public void applyPetsitter(SitterDTO dto);

    @Select("select APP_NO,USER_NO,HISTORY,LICENSE,INFO FROM P_SITTER_APP where USER_NO=#{user_no}")
    public SitterDTO getAppSitter(@Param("user_no")String Userno);

    @Update("UPDATE P_SITTER_APP set HISTORY=#{history}, LICENSE=#{license}, INFO=#{info}, updated_at=SYSDATE where USER_NO=#{user_no}")
    public void updateAppSitter(SitterDTO dto);
}
