package com.sist.web.sitterchat.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

import com.sist.web.sitterchat.vo.*;

@Mapper
public interface SitterChatMapper {

    // 채팅방 목록 조회
    @Select(
        "SELECT r.*, "
        + "u.nickname AS opponent_nickname, "
        + "u.profile AS opponent_profile, "
        + "sr.res_date AS res_date, "
        + "sr.start_time AS start_time_str, "
        + "sr.end_time AS end_time_str "
        + "FROM ( "
        + "  SELECT r.*, ROWNUM AS num "
        + "  FROM ( "
        + "    SELECT * "
        + "    FROM p_sitter_chat_room "
        + "    WHERE user1_no = #{userNo} OR user2_no = #{userNo} "
        + "    ORDER BY created_at DESC "
        + "  ) r "
        + ") r "
        + "JOIN p_users u "
        + "  ON u.user_no = CASE WHEN r.user1_no = #{userNo} THEN r.user2_no ELSE r.user1_no END "
        + "JOIN p_sitter_res sr ON r.reserve_no = sr.res_no "
        + "WHERE num BETWEEN #{start} AND #{end}"
    )
    @Results(id = "chatRoomResultMap", value = {
        @Result(property = "opponentNickname", column = "opponent_nickname"),
        @Result(property = "opponentProfile", column = "opponent_profile"),
        @Result(property = "resDate", column = "res_date"),
        @Result(property = "startTimeStr", column = "start_time_str"),
        @Result(property = "endTimeStr", column = "end_time_str")
    })
    public List<SitterChatRoomVO> SitterChatRoomList(@Param("userNo") int userNo,@Param("start") int start,@Param("end") int end);
    
    // 채팅방 목록 검색
    @Select(
    	    "SELECT r.*, "
    	    + "u.nickname AS opponent_nickname, "
    	    + "u.profile AS opponent_profile, "
    	    + "sr.res_date AS res_date, "
    	    + "sr.start_time AS start_time_str, "
    	    + "sr.end_time AS end_time_str "
    	    + "FROM ( "
    	    + "  SELECT r.*, ROWNUM AS num "
    	    + "  FROM ( "
    	    + "    SELECT * FROM p_sitter_chat_room "
    	    + "    WHERE (user1_no = #{userNo} OR user2_no = #{userNo}) "
    	    + "    AND ( "
    	    + "      (#{fd} = 'nickname' AND EXISTS ( "
    	    + "        SELECT 1 FROM p_users u "
    	    + "        WHERE u.user_no = CASE WHEN user1_no = #{userNo} THEN user2_no ELSE user1_no END "
    	    + "        AND u.nickname LIKE '%' || #{st} || '%' "
    	    + "      )) "
    	    + "      OR "
    	    + "      (#{fd} = 'res_date' AND EXISTS ( "
    	    + "        SELECT 1 FROM p_sitter_res sr "
    	    + "        WHERE sr.res_no = reserve_no "
    	    + "        AND TO_CHAR(sr.res_date, 'YYYY-MM-DD') LIKE '%' || #{st} || '%' "
    	    + "      )) "
    	    + "    ) "
    	    + "    ORDER BY created_at DESC "
    	    + "  ) r "
    	    + ") r "
    	    + "JOIN p_users u "
    	    + "  ON u.user_no = CASE WHEN r.user1_no = #{userNo} THEN r.user2_no ELSE r.user1_no END "
    	    + "JOIN p_sitter_res sr ON r.reserve_no = sr.res_no "
    	    + "WHERE num BETWEEN #{start} AND #{end}"
    	)
    	@ResultMap("chatRoomResultMap")
    	public List<SitterChatRoomVO> SitterChatRoomListWithFilter(Map<String, Object> map);

    // 채팅방 총 페이지 수
    @Select(
        "SELECT CEIL(COUNT(*) / #{rowSize}) "
        + "FROM p_sitter_chat_room "
        + "WHERE user1_no = #{userNo} OR user2_no = #{userNo}"
    )
    public int SitterChatRoomTotalPage(@Param("userNo") int userNo,@Param("rowSize") int rowSize);
    
    // 채팅방 검색 총 페이지 수 
    @Select(
    	    "SELECT CEIL(COUNT(*) / #{rowSize}) "
    	    + "FROM p_sitter_chat_room r "
    	    + "WHERE (user1_no = #{userNo} OR user2_no = #{userNo}) "
    	    + "AND ( "
    	    + "  (#{fd} = 'nickname' AND EXISTS ( "
    	    + "    SELECT 1 FROM p_users u "
    	    + "    WHERE u.user_no = CASE WHEN r.user1_no = #{userNo} THEN r.user2_no ELSE r.user1_no END "
    	    + "    AND u.nickname LIKE '%' || #{st} || '%' "
    	    + "  )) "
    	    + "  OR "
    	    + "  (#{fd} = 'res_date' AND EXISTS ( "
    	    + "    SELECT 1 FROM p_sitter_res sr "
    	    + "    WHERE sr.res_no = r.reserve_no "
    	    + "    AND TO_CHAR(sr.res_date, 'YYYY-MM-DD') LIKE '%' || #{st} || '%' "
    	    + "  )) "
    	    + ")"
    	)
    	public int SitterChatRoomTotalPageWithFilter(Map<String, Object> map);

    // 채팅방 생성
    @Insert(
        "INSERT INTO p_sitter_chat_room ("
        + "room_id, reserve_no, user1_no, user2_no, start_time, end_time"
        + ") VALUES ("
        + "p_sitchat_roomid_seq.NEXTVAL, "
        + "#{reserve_no}, #{user1_no}, #{user2_no}, #{start_time}, #{end_time}"
        + ")"
    )
    public int SitterChatRoomInsert(SitterChatRoomVO vo);

    // 채팅방 삭제
    @Delete(
        "DELETE FROM p_sitter_chat_room "
        + "WHERE room_id = #{room_id}"
    )
    public int SitterChatRoomDelete(@Param("room_id") int room_id);

    // 메시지 저장
    @Insert(
        "INSERT INTO p_sitter_chat ("
        + "chat_no, room_id, sender_no, receiver_no, chat_content, chat_type"
        + ") VALUES ("
        + "p_sitchat_no_seq.NEXTVAL, "
        + "#{room_id}, #{sender_no}, #{receiver_no}, #{chat_content}, #{chat_type}"
        + ")"
    )
    public int SitterChatInsert(SitterChatVO vo);

    // 메시지 목록 조회
    @Select(
        "SELECT c.*, "
        + "u.user_no AS user_no, "
        + "u.nickname AS nickname, "
        + "u.profile AS profile "
        + "FROM p_sitter_chat c "
        + "JOIN p_users u ON c.sender_no = u.user_no "
        + "WHERE c.room_id = #{room_id} "
        + "ORDER BY c.send_at ASC"
    )
    @Results(id = "chatMessageResultMap", value = {
        @Result(property = "userVO.user_no", column = "user_no"),
        @Result(property = "userVO.nickname", column = "nickname"),
        @Result(property = "userVO.profile", column = "profile")
    })
    public List<SitterChatVO> SitterChatList(@Param("room_id") int room_id);

    // 메시지 키워드 검색
    @Select(
        "SELECT c.*, "
        + "u.user_no AS user_no, "
        + "u.nickname AS nickname, "
        + "u.profile AS profile "
        + "FROM p_sitter_chat c "
        + "JOIN p_users u ON c.sender_no = u.user_no "
        + "WHERE c.room_id = #{room_id} "
        + "AND c.chat_content LIKE '%' || #{keyword} || '%' "
        + "ORDER BY c.send_at ASC"
    )
    @ResultMap("chatMessageResultMap")
    public List<SitterChatVO> SitterChatSearch(@Param("room_id") int room_id,@Param("keyword") String keyword);
}