package com.sist.web.sitter.vo;
import java.util.*;

import com.sist.web.user.vo.UserVO;

import lombok.Data;

@Data
public class SitterResVO {
    // 예약 기본 정보
    private int res_no, user_no, sitter_no, total_price;
    private Date res_date, created_at;
    private String start_time, end_time, location_type, location_detail, res_status, pay_status, imp_uid;
    private List<Integer> pet_nos; // 예약 시 선택된 반려동물 번호 목록
    
    private UserVO userVO;
    private SitterVO sitterVO;
    private List<PetsVO> petsList;
}