package com.sist.web.sitterchat.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SitterChatMessageDTO {
	@NotNull
	private int roomId;
    private int senderNo;
    @NotNull
    private int receiverNo;
    @NotBlank
    private String chatContent;
    @NotBlank
    private String chatType;
}
