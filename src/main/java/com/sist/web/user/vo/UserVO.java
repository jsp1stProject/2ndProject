package com.sist.web.user.vo;

import lombok.Data;

import java.util.Date;
@Data
public class UserVO {
    private String user_mail,social_id,password,user_name,nickname,phone;
    private int enabled;
    private Long user_no;
    private Date birthday,regdate;
}
