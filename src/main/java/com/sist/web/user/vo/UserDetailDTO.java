package com.sist.web.user.vo;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Date;

@Getter
@Service
public class UserDetailDTO {
    private String user_mail,social_id,user_name,nickname,phone,profile;
    private int enabled;
    private Long user_no;
    private Date birthday,regdate;
    private String authority;//p_autority 테이블 join
}
