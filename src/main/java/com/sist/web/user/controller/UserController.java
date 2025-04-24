package com.sist.web.user.controller;

import com.sist.web.user.service.UserService;
import com.sist.web.user.service.UserTransactionalService;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/main")
    public String main_home(Model model){
        model.addAttribute("main_jsp", "home.jsp");
        return "main/main";
    }

    //권한 테스트용 페이지
    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(Model model) {
        model.addAttribute("main_jsp", "home.jsp");
        return "main/main";
    }

    //권한 테스트용 페이지
    @GetMapping("users/test")
    @PreAuthorize("hasRole('USER')")
    public String test(Model model) {

        return "main/main";
    }

    @GetMapping("login")
    public String login() {

        return "user/login";
    }

    @GetMapping("auth/join")
    public String join(@RequestParam(required = false, value="code") String code, Model model) {
        return "user/join";
    }

}
