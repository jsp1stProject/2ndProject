package com.sist.web.groupchat.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class GroupChatDTO {
	private Long message_no;
	@NotNull(message = "그룹 번호는 필수입니다")
	private Integer group_no;
	@NotNull(message = "보낸 사람 번호는 필수입니다")
	private Integer sender_no;
	private String content, message_type, is_deleted, sender_nickname;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp sent_at;
}
