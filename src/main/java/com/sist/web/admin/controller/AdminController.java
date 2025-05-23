package com.sist.web.admin.controller;

import com.sist.web.admin.service.AdminService;
import com.sist.web.mypage.vo.SitterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("")
    public String admin(Model model){
        model.addAttribute("main_jsp","../admin/petsitter_list.jsp");
        return "redirect:/admin/users";
    }

    @GetMapping("/users")
    public String userList(Model model,
                           @RequestParam(value = "page", required = false) String page,
                           @RequestParam(value = "mail", required = false) String mail,
                           @RequestParam(value = "loc", required = false) String loc){
        Map query=new HashMap();
        query.put("mail",mail);
        model.addAttribute("result",adminService.getAllUsers(page, query));
        model.addAttribute("menu","1");
        model.addAttribute("main_jsp","../admin/user_list.jsp");
        model.addAttribute("title","회원");
        return "admin/main";
    }

    @GetMapping("/users/{userno:[0-9]+}")
    public String userDetail(Model model,
                             @PathVariable String userno){
        model.addAttribute("result",adminService.getUserDetail(userno).get("user"));
        model.addAttribute("result2",adminService.getPetsDetail(userno));
        model.addAttribute("result3",adminService.getUserDetail(userno).get("group"));
        model.addAttribute("menu","1");
        model.addAttribute("main_jsp","../admin/user_detail.jsp");
        model.addAttribute("title","회원 정보");
        return "admin/main";
    }

    @GetMapping("/petsitters")
    public String petsitterList(Model model,
                                @RequestParam(value = "page", required = false) String page,
                                @RequestParam(value = "mail", required = false) String mail,
                                @RequestParam(value = "loc", required = false) String loc){
        Map query=new HashMap();
        query.put("mail",mail);
        model.addAttribute("result",adminService.getAllSitters(page, query));
        model.addAttribute("menu","2-1");
        model.addAttribute("main_jsp","../admin/petsitter_list.jsp");
        model.addAttribute("title","펫시터 목록");
        return "admin/main";
    }
    
    @GetMapping("/petsitters/{userno:[0-9]+}/applications")
    public String petsitterAppDetail(Model model,
                             @PathVariable String userno){
        model.addAttribute("result",adminService.getSitterAppDetail(userno));
        model.addAttribute("result2",adminService.getPetsDetail(userno));
        model.addAttribute("menu","2-2");
        model.addAttribute("main_jsp","../admin/petsitterapp_detail.jsp");
        model.addAttribute("title","펫시터 신청서");
        return "admin/main";
    }

    @GetMapping("/petsitters/applications")
    public String petsitterAppList(Model model,
                                @RequestParam(value = "page", required = false) String page,
                                @RequestParam(value = "mail", required = false) String mail,
                                @RequestParam(value = "loc", required = false) String loc){
        Map query=new HashMap();
        query.put("mail",mail);
        model.addAttribute("result",adminService.getSitterApps(page, query));
        model.addAttribute("menu","2-2");
        model.addAttribute("main_jsp","../admin/petsitterapp_list.jsp");
        model.addAttribute("title","펫시터 신청서");
        return "admin/main";
    }
}
