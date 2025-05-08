package com.sist.web.groupchat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserConnectionInfoDTO {
    private long userNo;
    private long groupNo;
    private String nickname;
}