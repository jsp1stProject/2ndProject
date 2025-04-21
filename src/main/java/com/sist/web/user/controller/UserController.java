package com.sist.web.user.controller;

import com.sist.web.user.service.UserService;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(Model model) {
        return "main/home";
    }
    @GetMapping("users/test")
    @PreAuthorize("hasRole('USER')")
    public String test(Model model) {
        return "main/home";
    }

    @GetMapping("login")
    public String login() {
        return "user/login";
    }


    @GetMapping("auth/join")
    public String join(@RequestParam(required = false, value="code") String code, Model model) {
        return "user/join";
    }

    @PostMapping("join/join_ok.do")
    public String join_ok(@ModelAttribute UserVO vo, Model model) {
        System.out.println("original: "+vo.getPassword());
        vo.setPassword(passwordEncoder.encode(vo.getPassword()));
        System.out.println("encoded: "+vo.getPassword());
        userService.insertDefaultUser(vo);
        model.addAttribute("user_name", vo.getUser_name());
        return "user/join_ok";
    }

}
