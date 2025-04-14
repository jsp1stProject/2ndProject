package com.sist.vo;

import lombok.Data;

import java.util.Date;
@Data
public class UserVO {
    private String user_mail,social_id,password,user_name,nickname,phone;
    private int user_no,enabled;
    private Date birthday,regdate;
}
