package com.sist.web.sitterchat.vo;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class SitterChatRoomVO {
    private int room_no;
    private int user1_no;
    private int user2_no;
    private int reserve_no;
    private Timestamp create_date;
    private String is_active;

    // 프론트용 추가 필드 (상대방 정보)
    private String opponent_nick;
    private String opponent_profile;
    private String reserve_start_time;
}

