package com.sist.web.user.service;

import com.sist.web.user.vo.UserVO;

import java.util.Map;

public interface UserService {
    public void insertDefaultUser(UserVO vo);
    public Map<String, Object> loginUser(UserVO vo);
}
