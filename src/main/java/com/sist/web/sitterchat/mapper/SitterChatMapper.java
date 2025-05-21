package com.sist.web.sitterchat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.sist.web.sitterchat.vo.*;

@Mapper
public interface SitterChatMapper {

    // 채팅방 목록 조회 (상대방 정보 포함)
    @Select("SELECT scr.*, " +
            "CASE WHEN scr.user1_no = #{user_no} THEN u2.nickname ELSE u1.nickname END AS opponent_nick, " +
            "CASE WHEN scr.user1_no = #{user_no} THEN u2.profile ELSE u1.profile END AS opponent_profile, " +
            "sr.start_time AS reserve_start_time " +
            "FROM p_sitter_chat_room scr " +
            "JOIN p_users u1 ON scr.user1_no = u1.user_no " +
            "JOIN p_users u2 ON scr.user2_no = u2.user_no " +
            "JOIN p_sitter_res sr ON scr.reserve_no = sr.res_no " +
            "WHERE scr.user1_no = #{user_no} OR scr.user2_no = #{user_no} " +
            "ORDER BY scr.create_date DESC")
    @Results({
        @Result(property = "room_no", column = "room_no"),
        @Result(property = "user1_no", column = "user1_no"),
        @Result(property = "user2_no", column = "user2_no"),
        @Result(property = "reserve_no", column = "reserve_no"),
        @Result(property = "create_date", column = "create_date"),
        @Result(property = "is_active", column = "is_active"),
        @Result(property = "opponent_nick", column = "opponent_nick"),
        @Result(property = "opponent_profile", column = "opponent_profile"),
        @Result(property = "reserve_start_time", column = "reserve_start_time")
    })
    public List<SitterChatRoomVO> selectChatRoomList(@Param("user_no") int user_no);
    
    // 목록 페이지
    @Select("SELECT CEIL(COUNT(*) / 10.0) FROM p_sitter_chat_room")
    public int selectChatRoomTotalPage();
    
    // 채팅방 단건 조회 (사이드바 목록 클릭용)
    @Select("SELECT * FROM p_sitter_chat_room WHERE room_no = #{room_no}")
    public SitterChatRoomVO SitterChatRoomById(@Param("room_no") int room_no);
    
    // 채팅방 단건 조회 (중복 생성 방지용)
    @Select("SELECT * FROM p_sitter_chat_room " +
            "WHERE user1_no = #{user1_no} AND user2_no = #{user2_no} AND reserve_no = #{reserve_no}")
    public SitterChatRoomVO selectChatRoom(@Param("user1_no") int user1_no,
                                           @Param("user2_no") int user2_no,
                                           @Param("reserve_no") int reserve_no);

    // 채팅방 생성
    @Insert("INSERT INTO p_sitter_chat_room (room_no, user1_no, user2_no, reserve_no) " +
            "VALUES (p_sitchat_roomno_seq.NEXTVAL, #{user1_no}, #{user2_no}, #{reserve_no})")
    @Options(useGeneratedKeys = true, keyProperty = "room_no")
    public int insertChatRoom(SitterChatRoomVO vo);

    // 채팅 메시지 목록 조회
    @Select("SELECT c.*, u.nickname AS sender_nick, u.profile AS sender_profile " +
            "FROM p_sitter_chat c " +
            "JOIN p_users u ON c.sender_no = u.user_no " +
            "WHERE c.room_no = #{room_no} " +
            "ORDER BY c.send_time ASC")
    @Results({
        @Result(property = "chat_no", column = "chat_no"),
        @Result(property = "room_no", column = "room_no"),
        @Result(property = "sender_no", column = "sender_no"),
        @Result(property = "content", column = "content"),
        @Result(property = "send_time", column = "send_time"),
        @Result(property = "read_flag", column = "read_flag"),
        @Result(property = "sender_nick", column = "sender_nick"),
        @Result(property = "sender_profile", column = "sender_profile")
    })
    public List<SitterChatVO> selectChatList(@Param("room_no") int room_no);

    // 채팅 메시지 저장
    @Insert("INSERT INTO p_sitter_chat (chat_no, room_no, sender_no, content) " +
            "VALUES (p_sitchat_no_seq.NEXTVAL, #{room_no}, #{sender_no}, #{content})")
    public int insertChat(SitterChatVO vo);

    // 채팅방 삭제 (비활성화 시)
    @Delete("DELETE FROM p_sitter_chat_room WHERE room_no = #{room_no}")
    public int deleteChatRoom(@Param("room_no") int room_no);
    
    // 메시지 검색
    @Select("SELECT c.*, u.nickname AS sender_nick, u.profile AS sender_profile " +
            "FROM p_sitter_chat c " +
            "JOIN p_users u ON c.sender_no = u.user_no " +
            "WHERE c.room_no = #{room_no} AND LOWER(c.content) LIKE '%' || LOWER(#{keyword}) || '%' " +
            "ORDER BY c.send_time ASC")
    @Results({
        @Result(property = "chat_no", column = "chat_no"),
        @Result(property = "room_no", column = "room_no"),
        @Result(property = "sender_no", column = "sender_no"),
        @Result(property = "content", column = "content"),
        @Result(property = "send_time", column = "send_time"),
        @Result(property = "read_flag", column = "read_flag"),
        @Result(property = "sender_nick", column = "sender_nick"),
        @Result(property = "sender_profile", column = "sender_profile")
    })
    public List<SitterChatVO> searchChatByKeyword(@Param("room_no") int room_no,
                                                  @Param("keyword") String keyword);
    
    // 채팅 가능 여부
    @Select("SELECT CASE " +
            "WHEN (sr.res_end_time + (1/24)) > SYSDATE THEN 'Y' ELSE 'N' " +
            "END AS chat_enabled " +
            "FROM p_sitter_chat_room scr " +
            "JOIN p_sitter_res sr ON scr.reserve_no = sr.res_no " +
            "WHERE scr.room_no = #{room_no}")
    public String isChatEnabled(@Param("room_no") int room_no);

}
