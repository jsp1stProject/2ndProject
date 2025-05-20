package com.sist.web.sitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.sitter.service.SitterResPetService;
import com.sist.web.sitter.service.SitterResService;
import com.sist.web.sitter.service.SitterService;
import com.sist.web.sitter.vo.SitterVO;
import com.sist.web.user.vo.UserVO;

import java.util.*;
import com.sist.web.sitter.vo.*;
@Controller
public class SitterResController {
    @Autowired
	private SitterService sService;
    
    @Autowired
    private SitterResPetService pService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
	@GetMapping("/sitter/resList")
	public String sitter_resList()
	{
        return "sitter/resList";
	}
	@GetMapping("/sitter/resDetail")
	public String sitter_resDetail()
	{
        return "sitter/resDetail";
	}
	
	@GetMapping("/sitter/reserve")
	public String sitter_reserve(@RequestParam int sitter_no,
	                             @CookieValue(name = "accessToken", required = false) String token,
	                             Model model)
	{

	    if (token == null || token.isEmpty()) {
	        System.out.println("❌ 토큰이 없음. 로그인 필요.");
	        return "redirect:/login";
	    }
	    
	    int user_no = -1; // 에러 탐지용 기본값
	    try {
	        user_no = Integer.parseInt(jwtTokenProvider.getUserNoFromToken(token));
	    } catch (Exception e) {
	        return "redirect:/login";
	    }

	    // 펫시터 정보 (가격)
	    SitterVO sitterVO = sService.sitterDetailData(sitter_no);
	    int petFirstPrice = Integer.parseInt(
	            sitterVO.getPet_first_price().replaceAll("[^0-9]", "")
	    );

	    // 신청자의 반려동물 리스트
	    List<PetsVO> petsList = pService.getPetsByUserNo(user_no);
	    System.out.println("✅ user_no: " + user_no);
	    System.out.println("✅ petsList size: " + petsList.size());
	    for (PetsVO pet : petsList) {
	        System.out.println("➡️ pet_name: " + pet.getPet_name());
	    }
	    String petsJson = new Gson().toJson(petsList);

	    model.addAttribute("petFirstPrice", petFirstPrice);
	    model.addAttribute("petsJson", petsJson);
	    model.addAttribute("sitterNo", sitter_no);

	    return "sitter/reserve";
	}

}
