package com.sist.web.chat.group.vo;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class GroupChatVO {
	private long message_no;
	private int group_no, sender_no;
	private String content, message_type, is_deleted, sender_nickname;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp sent_at;
}
