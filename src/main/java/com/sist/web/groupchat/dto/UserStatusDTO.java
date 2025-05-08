package com.sist.web.groupchat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class UserStatusDTO {
    private Long userNo;
    private String nickname;
}