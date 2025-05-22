package com.sist.web.mypage.vo;

import lombok.Data;

@Data
public class PetVO {
    private String pet_no,pet_name, pet_profilepic, pet_type, pet_subtype,user_no;
    private int pet_age,pet_char1,pet_char2,pet_char3,pet_status;
    private float pet_weight;
}
