package com.sist.web.user.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
public class UserVO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String user_mail,social_id,password,user_name,nickname,phone,profile,addr,db_birthday;
    private int enabled;
    private Long user_no;
    private LocalDate birthday,regdate;
    private String authority;//p_autority 테이블 join
}
