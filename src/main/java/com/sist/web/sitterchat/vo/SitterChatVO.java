package com.sist.web.sitterchat.vo;

import java.sql.Timestamp;

import com.sist.web.sitter.vo.SitterResVO;
import com.sist.web.user.vo.UserVO;

import lombok.Data;

import lombok.Data;
import java.util.Date;

@Data
public class SitterChatVO {
    private int chat_no;
    private int room_no;
    private int sender_no;
    private String content;
    private Timestamp send_time;
    private String read_flag;

    // 프론트 표시용
    private String sender_nick;
    private String sender_profile;
}
