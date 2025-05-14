package com.sist.web.mypage.service;

import com.sist.web.user.vo.UserDetailDTO;

public interface MypageService {
    public String getValidUserNo(String token);
    public UserDetailDTO getMyinfo(String token);
    public void updateMyinfo(String token, UserDetailDTO dto);
}
