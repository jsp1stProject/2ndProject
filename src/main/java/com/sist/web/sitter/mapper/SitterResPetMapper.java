package com.sist.web.sitter.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.sist.web.sitter.vo.PetsVO;

public interface SitterResPetMapper {

    @Insert("INSERT INTO p_sitter_res_pet (res_no, pet_no) VALUES (#{res_no}, #{pet_no})")
    public void insertResPet(@Param("res_no") int res_no, @Param("pet_no") int pet_no);

    @Select("SELECT p.*, u.user_no AS user_user_no, u.nickname AS user_nickname " 
            +"FROM p_sitter_res_pet rp " 
            +"JOIN p_pets p ON rp.pet_no = p.pet_no " 
            +"JOIN p_users u ON p.user_no = u.user_no " 
            +"WHERE rp.res_no = #{res_no}")
    @Results({
        @Result(property = "pet_no", column = "pet_no"),
        @Result(property = "user_no", column = "user_no"),
        @Result(property = "pet_name", column = "pet_name"),
        @Result(property = "pet_age", column = "pet_age"),
        @Result(property = "pet_type", column = "pet_type"),
        @Result(property = "pet_subtype", column = "pet_subtype"),
        @Result(property = "pet_profilepic", column = "pet_profilepic"),
        @Result(property = "pet_char1", column = "pet_char1"),
        @Result(property = "pet_char2", column = "pet_char2"),
        @Result(property = "pet_char3", column = "pet_char3"),
        @Result(property = "pet_status", column = "pet_status"),
        @Result(property = "user.user_no", column = "user_user_no"),
        @Result(property = "user.nickname", column = "user_nickname")
    })
    public List<PetsVO> getPetsByResNo(@Param("res_no") int res_no);
} 
