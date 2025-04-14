package com.sist.web;

import com.sist.service.UserService;
import com.sist.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public String join() {
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
