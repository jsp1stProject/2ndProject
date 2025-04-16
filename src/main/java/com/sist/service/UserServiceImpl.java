package com.sist.service;

import com.sist.mapper.UserMapper;
import com.sist.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void insertDefaultUser(UserVO vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_mail", vo.getUser_mail());
        map.put("authority", "ROLE_USER");
        userMapper.insertDefaultUser(vo);
        userMapper.insertUserAuthority(map);
    }

    @Override
    public Map<String, Object> loginUser(UserVO vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_mail", vo.getUser_mail());
        int isUser = userMapper.getUserMailCount(vo.getUser_mail());
        if(isUser == 0) {
            map.put("state", "noid");
        }else{
            String password = vo.getPassword();
            System.out.println("orig: "+vo.getPassword());

            String dbPassword = userMapper.getPassword(vo.getUser_mail());
            System.out.println("db: "+dbPassword);
            if(passwordEncoder.matches(password,dbPassword)) {
                map.put("state", "ok");
                map.put("user_mail", vo.getUser_mail());
            }else{
                map.put("state", "no");
            }
        }
        System.out.println("login api : "+map);
        return map;
    }

}
