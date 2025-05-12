package com.sist.web.sitter.mapper;

import java.util.*;

import org.apache.ibatis.annotations.*;
import com.sist.web.sitter.vo.*;

public interface SitterResMapper {

    // 예약하기

    @Insert("INSERT INTO p_sitter_res (res_no, user_no, sitter_no, res_date, start_time, end_time, "
            +"location_type, location_detail, total_price, pay_status, res_status, created_at) " 
            +"VALUES (#{res_no}, #{user_no}, #{sitter_no}, #{res_date}, #{start_time}, #{end_time}, " 
            +"#{location_type}, #{location_detail}, #{total_price}, #{pay_status}, #{res_status}, SYSDATE)")
    @SelectKey(statement = "SELECT p_sitter_res_seq.NEXTVAL FROM dual", keyProperty = "res_no", before = true, resultType = int.class)
    public void insertSitterRes(SitterResVO vo);

    // 예약 상세

    @Select("SELECT r.*, " 
            +"u.user_no AS user_user_no, u.nickname AS user_nickname, u.profile_pic AS user_profile_pic, " 
            +"s.sitter_no AS sitter_sitter_no, s.pet_first_price AS sitter_pet_first_price " 
            +"FROM p_sitter_res r "
            +"JOIN p_users u ON r.user_no = u.user_no "
            +"JOIN p_sitter s ON r.sitter_no = s.sitter_no " 
            +"WHERE r.res_no = #{res_no}")
    @Results(id = "sitterResResultMap", value = {
        @Result(property = "res_no", column = "res_no"),
        @Result(property = "user_no", column = "user_no"),
        @Result(property = "sitter_no", column = "sitter_no"),
        @Result(property = "res_date", column = "res_date"),
        @Result(property = "start_time", column = "start_time"),
        @Result(property = "end_time", column = "end_time"),
        @Result(property = "location_type", column = "location_type"),
        @Result(property = "location_detail", column = "location_detail"),
        @Result(property = "total_price", column = "total_price"),
        @Result(property = "pay_status", column = "pay_status"),
        @Result(property = "res_status", column = "res_status"),
        @Result(property = "created_at", column = "created_at"),
        @Result(property = "userVO.user_no", column = "user_user_no"),
        @Result(property = "userVO.nickname", column = "user_nickname"),
        @Result(property = "userVO.profile_pic", column = "user_profile_pic"),
        @Result(property = "sitterVO.sitter_no", column = "sitter_sitter_no"),
        @Result(property = "sitterVO.pet_first_price", column = "sitter_pet_first_price")
    })
    SitterResVO sitterReservation(@Param("res_no") int res_no);

    // 예약 목록

    @Select("SELECT r.*, " 
            +"u.user_no AS user_user_no, u.nickname AS user_nickname, u.profile_pic AS user_profile_pic, " 
            +"s.sitter_no AS sitter_sitter_no, s.pet_first_price AS sitter_pet_first_price " 
            +"FROM p_sitter_res r " 
            +"JOIN p_users u ON r.user_no = u.user_no " 
            +"JOIN p_sitter s ON r.sitter_no = s.sitter_no " 
            +"WHERE r.user_no = #{user_no} " 
            +"ORDER BY r.res_date DESC, r.start_time ASC")
    @ResultMap("sitterResResultMap")
    List<SitterResVO> sitterReservationList(@Param("user_no") int user_no);
    
    // 펫 목록
    @Select("SELECT * FROM p_pets WHERE user_no = #{user_no}")
    public List<PetsVO> getPetsByUserNo(@Param("user_no") int user_no);
} 


