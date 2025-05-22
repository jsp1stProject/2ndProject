package com.sist.web.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserDetailDTO {
    private String user_mail, social_id, user_name, nickname, phone, profile, addr, orig_pwd, new_pwd, db_birthday, db_regdate;
    private int enabled;
    private Long user_no;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date regdate, birthday;
}