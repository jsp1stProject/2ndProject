package com.sist.web.chat.group.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupMemberVO {
	private long group_id, last_read_message;
	private String user_id, nickname, role, status;
	private LocalDateTime joined_at, left_at;
}
