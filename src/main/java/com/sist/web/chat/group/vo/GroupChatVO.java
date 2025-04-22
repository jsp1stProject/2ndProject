package com.sist.web.chat.group.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupChatVO {
	private long message_no;
	private int group_no, sender_no;
	private String content, message_type, is_deleted, sender_nickname;
	private LocalDateTime sent_at;
}
