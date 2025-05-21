package com.sist.web.sitterchat.vo;

import java.sql.Timestamp;

import com.sist.web.user.vo.UserVO;

import lombok.Data;

@Data
public class SitterChatVO {
	private int chat_no;            // 메시지 번호
    private int room_id;           // 채팅방 번호
    private int sender_no;         // 보낸 사람
    private int receiver_no;       // 받은 사람
    private String chat_content;   // 메시지 내용
    private String chat_type;      // 메시지 타입 (text, img 등)
    private Timestamp send_at;     // 보낸 시각
    private String is_deleted;     // 삭제 여부 (Y/N)
    
    private UserVO userVO;
}
