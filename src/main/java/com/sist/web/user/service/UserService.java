package com.sist.web.user.service;

import com.sist.web.user.vo.UserVO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    public void insertDefaultUser(UserVO vo);
    public void insertKakaoUser(UserVO vo);
    public Map<String, Object> loginUser(UserVO vo);
    public String GetKakaoAccessToken(String code);
    public ResponseEntity GetInsertKakaoUser(String kakaoAccessToken);
}
