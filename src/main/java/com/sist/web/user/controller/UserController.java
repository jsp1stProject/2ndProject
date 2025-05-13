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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/main")
    public String main_home(Model model){
        model.addAttribute("main_jsp", "home.jsp");
        return "main/main";
    }

    @GetMapping("/dev")
    public String dev_home(Model model){
        model.addAttribute("main_jsp", "dev.jsp");
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
    public String login(Model model) {
        model.addAttribute("main_jsp", "../user/login.jsp");
        return "main/main";
    }

    @PostMapping("logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        Cookie cookie2 = new Cookie("refreshToken", null);
        cookie2.setMaxAge(0);
        cookie2.setHttpOnly(true);
        cookie2.setPath("/");
        response.addCookie(cookie2);

        return "redirect:/main";
    }

    @GetMapping("auth/join")
    public String join(@RequestParam(required = false, value="code") String code, Model model) {
        model.addAttribute("main_jsp", "../user/join.jsp");
        return "main/main";
    }

    @GetMapping("chat/test")
    public String chat_test(Model model) {
        return "groupchat/chat_style";
    }

    @GetMapping("grouplist/test")
    public String grouplist_test(Model model) {
        model.addAttribute("main_jsp", "../group/listtest.jsp");
        return "main/main";
    }

    @GetMapping("groupdetail/test")
    public String groupdetail_test(Model model) {
        model.addAttribute("main_jsp", "../group/detailtest.jsp");
        return "main/main";
    }
}
