package com.sist.web.user.vo;

import lombok.Data;
import java.util.Date;

@Data
public class UserVO {
    private String user_mail,social_id,password,user_name,nickname,phone,profile,addr,db_birthday;
    private int enabled;
    private Long user_no;
    private Date birthday,regdate;
    private String authority;//p_autority 테이블 join
}
