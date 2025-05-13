package com.sist.web.user.service;

import com.sist.web.user.vo.UserDetailDTO;
import com.sist.web.user.vo.UserVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserService {
    public Map<String, Object> loginUser(UserVO vo);
    public String GetKakaoAccessToken(String code, String url);
    public void InsertOrLoginKakaoUser(String kakaoAccessToken,  HttpServletResponse res);
    public void CheckActiveUser(String userno);
    public UserDetailDTO GetUserDetail(String userno);
    public UserDetailDTO GetActiveUserDetail(String userno);
}
