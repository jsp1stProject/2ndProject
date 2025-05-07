package com.sist.web.user.service;

import com.sist.web.user.vo.UserVO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserService {
    public Map<String, Object> loginUser(UserVO vo);
    public String GetKakaoAccessToken(String code, String url);
    public ResponseEntity InsertOrLoginKakaoUser(String kakaoAccessToken,  HttpServletResponse res);
}
