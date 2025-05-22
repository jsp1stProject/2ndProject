package com.sist.web.sitter.mapper;

import java.util.*;

import org.apache.ibatis.annotations.*;
import com.sist.web.sitter.vo.*;

public interface SitterResMapper {

	// 예약하기
	@Insert("INSERT INTO p_sitter_res (res_no, user_no, sitter_no, res_date, start_time, end_time, "
			+ "location_type, location_detail, total_price, pay_status, res_status, created_at) "
			+ "VALUES (#{res_no}, #{user_no}, #{sitter_no}, #{res_date}, #{start_time}, #{end_time}, "
			+ "#{location_type}, #{location_detail}, #{total_price}, #{pay_status}, #{res_status}, SYSDATE)")
	@SelectKey(statement = "SELECT p_sitter_res_seq.NEXTVAL FROM dual", keyProperty = "res_no", before = true, resultType = int.class)
	public void insertSitterRes(SitterResVO vo);

	// 예약 상세
	@Select("SELECT r.*, "
			+ "u.user_no AS user_user_no, u.nickname AS user_nickname, u.profile AS user_profile_pic, "
			+ "s.sitter_no AS sitter_sitter_no, s.pet_first_price AS sitter_pet_first_price, " 
			+ "pu.nickname AS sitter_nickname "
			+ "FROM p_sitter_res r "
			+ "JOIN p_users u ON r.user_no = u.user_no " + "JOIN p_sitter s ON r.sitter_no = s.sitter_no "
			+ "JOIN p_users pu ON s.user_no = pu.user_no "
			+ "WHERE r.res_no = #{res_no}")
	@Results(id = "sitterResResultMap", value = { @Result(property = "res_no", column = "res_no"),
			@Result(property = "user_no", column = "user_no"), @Result(property = "sitter_no", column = "sitter_no"),
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
			@Result(property = "userVO.profile", column = "user_profile_pic"),
			@Result(property = "sitterVO.sitter_no", column = "sitter_sitter_no"),
			@Result(property = "sitterVO.pet_first_price", column = "sitter_pet_first_price") })
			@Result(property = "sitterVO.user.nickname", column = "sitter_nickname")
	public SitterResVO sitterReservation(@Param("res_no") int res_no);

	// 예약 목록
	@Select("SELECT r.*, "
			+ "u.user_no AS user_user_no, u.nickname AS user_nickname, u.profile AS user_profile_pic, "
			+ "s.sitter_no AS sitter_sitter_no, s.pet_first_price AS sitter_pet_first_price " + "FROM p_sitter_res r "
			+ "JOIN p_users u ON r.user_no = u.user_no " + "JOIN p_sitter s ON r.sitter_no = s.sitter_no "
			+ "WHERE r.user_no = #{user_no} " + "ORDER BY r.res_date DESC, r.start_time ASC")
	@ResultMap("sitterResResultMap")
	public List<SitterResVO> sitterReservationList(@Param("user_no") int user_no);

	// 채팅방용 user2 가져오기
    @Select("SELECT user_no FROM p_sitter WHERE sitter_no = #{sitter_no}")
    public int getSitterUserNoBySitterNo(@Param("sitter_no") int sitter_no);
    
	// 결제 완료
	@Update("UPDATE p_sitter_res SET pay_status = #{status}, imp_uid = #{imp_uid} WHERE res_no = #{res_no}")
	public void updatePayStatus(@Param("res_no") int res_no, @Param("status") String status,
			@Param("imp_uid") String imp_uid);

	// 예약 중복 검사
	@Select("SELECT COUNT(*) " + "FROM p_sitter_res " + "WHERE sitter_no = #{sitter_no} "
			+ "AND res_date = #{res_date} " + "AND res_status != '취소' "
			+ "AND (start_time < #{end_time} AND end_time > #{start_time})")
	public int checkReservationConflict(@Param("sitter_no") int sitter_no, @Param("res_date") Date res_date,
			@Param("start_time") String start_time, @Param("end_time") String end_time);

	// 펫시터가 예약 취소
	@Update("UPDATE p_sitter_res " + "SET res_status = '취소' " + "WHERE res_no = #{res_no}")
	public void cancelReservationBySitter(@Param("res_no") int res_no);

	// 해당 시터의 예약된 시간대 조회 (시간 제한용)
	@Select("SELECT start_time, end_time " + "FROM p_sitter_res " + "WHERE sitter_no = #{sitter_no} "
			+ "AND res_date = #{res_date} " + "AND res_status != '취소'")
	public List<Map<String, String>> getReservedTimeRanges(@Param("sitter_no") int sitter_no,
			@Param("res_date") Date res_date);

}
