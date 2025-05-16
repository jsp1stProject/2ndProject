package com.sist.web.user.service;

import com.sist.web.user.vo.UserDetailDTO;
import com.sist.web.user.vo.UserVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserService {
    public Map<String, Object> loginUser(UserVO vo);
    public String getKakaoAccessToken(String code, String url);
    public void insertOrLoginKakaoUser(String kakaoAccessToken, HttpServletResponse res);
    public void checkActiveUser(String userno);
    public UserDetailDTO getUserDetail(String userno);
    public UserDetailDTO getActiveUserDetail(String userno);
    public UserDetailDTO getHeaderDetail(String token);
}
