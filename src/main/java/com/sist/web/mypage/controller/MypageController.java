package com.sist.web.mypage.controller;

import com.sist.web.common.response.ApiResponse;
import com.sist.web.mypage.vo.PetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/mypage")
public class MypageController {

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public String mypage(Model model){
        return "redirect:/mypage/profile";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public String profiles(Model model){
        model.addAttribute("main_jsp", "../mypage/info.jsp");
        return "main/main";
    }

    @GetMapping("/pets/list")
    @PreAuthorize("hasRole('USER')")
    public String pets_list(Model model){
        model.addAttribute("main_jsp", "../mypage/pets.jsp");
        return "main/main";
    }

    @GetMapping("/pets/{petno}")
    public String GetMyPetDetail(Model model){
        model.addAttribute("main_jsp", "../mypage/pets_detail.jsp");
        return "main/main";
    }

    @GetMapping("/pets/new")
    @PreAuthorize("hasRole('USER')")
    public String pets_insert(Model model){
        model.addAttribute("main_jsp", "../mypage/pets_insert.jsp");
        return "main/main";
    }

    @GetMapping("/petsitters")
    public String petsitters(Model model){
        model.addAttribute("main_jsp", "../mypage/petsitters.jsp");
        return "main/main";
    }

    @GetMapping("/delete")
    public String delete(Model model){
        model.addAttribute("main_jsp", "../mypage/delete.jsp");
        return "main/main";
    }


}
