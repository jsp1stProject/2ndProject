package com.sist.web.sitterchat.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SitterChatMessageDTO {

    @NotNull
    private Integer roomId;

    @NotNull
    private Integer receiverNo;

    @NotBlank
    private String chatContent;

    @NotBlank
    private String chatType;

    private Integer senderNo;  // 서버에서 set (principal 기반)

    private String sendAt;     // optional: 프론트 표시용
}

