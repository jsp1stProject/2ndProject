package com.sist.web.chat.group.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupChatVO {
	private long message_id, group_id;
	private String sender_id, content, message_type, is_deleted;
	private LocalDateTime sent_at;
}
