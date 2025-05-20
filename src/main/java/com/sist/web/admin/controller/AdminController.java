package com.sist.web.admin.controller;

import com.sist.web.admin.service.AdminService;
import com.sist.web.mypage.vo.SitterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        return "admin/main";
    }

    @GetMapping("/petsitters/list")
    public String petsitterList(Model model,
                                @RequestParam(value = "page", required = false) String page,
                                @RequestParam(value = "mail", required = false) String mail,
                                @RequestParam(value = "loc", required = false) String loc){
        Map query=new HashMap();
        query.put("mail",mail);
        model.addAttribute("result",adminService.getAllSitters(page, query));
        model.addAttribute("main_jsp","../admin/petsitter_list.jsp");
        model.addAttribute("title","펫시터 목록");
        return "admin/main";
    }
    @GetMapping("/petsitters/application/list")
    public String petsitterAppList(Model model,
                                @RequestParam(value = "page", required = false) String page,
                                @RequestParam(value = "mail", required = false) String mail,
                                @RequestParam(value = "loc", required = false) String loc){
        Map query=new HashMap();
        query.put("mail",mail);
        model.addAttribute("result",adminService.getSitterApps(page, query));
        model.addAttribute("main_jsp","../admin/petsitterapp_list.jsp");
        model.addAttribute("title","펫시터 신청서");
        return "admin/main";
    }
}
