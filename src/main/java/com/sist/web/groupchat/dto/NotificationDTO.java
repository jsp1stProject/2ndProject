package com.sist.web.groupchat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class NotificationDTO {
	private String message;
	private int groupNo;
	private String senderNickname;
}
