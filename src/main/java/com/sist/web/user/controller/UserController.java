package com.sist.web.user.controller;

import com.sist.web.user.service.UserService;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("login")
    public String login() {
        return "user/login";
    }

    @GetMapping("join")
    public String join(@RequestParam(required = false, value="code") String code, Model model) {
        if(code != null && code.length() > 0) { //카카오 연동 가입이면
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String,String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("grant_type","authorization_code");
            requestBody.add("client_id","edc96d6c4e60c395ff9312d2ed6f71ba");
            requestBody.add("redirect_uri","http://localhost:8080/web/join/join.do");
            requestBody.add("code",code);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<?> response=restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );
            model.addAttribute("response", response.getBody());
        }


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
