package com.sist.service;

import com.sist.vo.UserVO;

import java.util.Map;

public interface UserService {
    public void insertDefaultUser(UserVO vo);
    public Map<String, Object> loginUser(UserVO vo);
}
