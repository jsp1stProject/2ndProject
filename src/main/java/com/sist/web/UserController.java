package com.sist.web;

import com.sist.service.UserService;
import com.sist.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("login.do")
    public String login() {
        return "user/login";
    }

    @GetMapping("join/join.do")
    public String join(@RequestParam(required = false, value="code") String code, Model model) {
        if(code != null && code.length() > 0) { //카카오 연동 가입이면

        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        Map<String,String> requestBody = new HashMap<>();
        requestBody.put("grant_type","authorization_code");
        requestBody.put("client_id","edc96d6c4e60c395ff9312d2ed6f71ba");
        requestBody.put("redirect_uri","http://localhost:8081/web/join/join.do");
        requestBody.put("code",code);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Void> response=restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMe
        )
        model.addAttribute("client_id", "edc96d6c4e60c395ff9312d2ed6f71ba");
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

    @GetMapping("join/join_ok.do")
    public String join_ok(Model model) {
        model.addAttribute("user_name", "권한확인");
        return "user/join_ok";
    }
}
