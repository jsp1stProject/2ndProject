package com.sist.web.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("")
    public String admin(){
        return "admin/main";
    }

    @GetMapping("/petsitters/list")
    public String petsitterList(Model model){

        model.addAttribute("main_jsp","../admin/petsitter_list.jsp");
        model.addAttribute("title","펫시터 목록");
        return "admin/main";
    }
}
