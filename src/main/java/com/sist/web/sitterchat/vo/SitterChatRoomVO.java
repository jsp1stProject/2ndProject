package com.sist.web.sitterchat.vo;
import java.sql.Timestamp;

import com.sist.web.sitter.vo.SitterResVO;
import com.sist.web.sitter.vo.SitterVO;
import com.sist.web.user.vo.UserVO;

import lombok.Data;
@Data
public class SitterChatRoomVO {
	private int room_id;         // 채팅방 번호
    private int reserve_no;      // 예약 번호
    private int user1_no;        // 예약자 (일반 유저)
    private int user2_no;        // 펫시터
    private Timestamp start_time;  // 채팅 시작 시각
    private Timestamp end_time;    // 채팅 종료 시각
    private Timestamp created_at;  // 생성 시각
    
    private UserVO userVO;               
    private SitterResVO sitterResVO;     
    private SitterVO sitterVO;
}
