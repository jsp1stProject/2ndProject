package com.sist.web.user.controller;

import com.sist.web.user.service.UserService;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserRestController {
    private final UserService userService;

    @PostMapping("/login")
    public Map<String, Object> auth_login(@ModelAttribute UserVO vo) {
        return userService.loginUser(vo);
    }
}
