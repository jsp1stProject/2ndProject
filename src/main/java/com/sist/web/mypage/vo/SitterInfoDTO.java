package com.sist.web.mypage.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class SitterInfoDTO {
    private int user_no,app_no,sitter_no,jjimcount,carecount,active;
    private float score;
    private String tag,content,sitter_pic,care_loc,pet_first_price,history,license;
    private Date created_at,updated_at;
}
