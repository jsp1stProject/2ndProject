package com.sist.web.chat.group.vo;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupMemberVO {
	private int group_no, user_no;
	private long last_read_message;
	private String nickname, role, status;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp joined_at, left_at;
}
