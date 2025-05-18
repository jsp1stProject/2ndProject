package com.sist.web.sitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.google.gson.Gson;
import com.sist.web.sitter.service.SitterResPetService;
import com.sist.web.sitter.service.SitterResService;
import com.sist.web.sitter.service.SitterService;
import com.sist.web.sitter.vo.SitterVO;
import java.util.*;
import com.sist.web.sitter.vo.*;
@Controller
public class SitterResController {
    @Autowired
	private SitterService sService;
    
    @Autowired
    private SitterResPetService pService;
    
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
    public String sitter_reserve(int sitter_no, int user_no, Model model) {
        // 펫시터 정보 (가격)
        SitterVO sitterVO = sService.sitterDetailData(sitter_no);
        int petFirstPrice = Integer.parseInt(
        	    sitterVO.getPet_first_price().replaceAll("[^0-9]", "")
        	);
        

        // 신청자의 반려동물 리스트
        List<PetsVO> petsList = pService.getPetsByUserNo(user_no);
        String petsJson = new Gson().toJson(petsList); 

        model.addAttribute("petFirstPrice", petFirstPrice);
        model.addAttribute("petsJson", petsJson);
        model.addAttribute("sitterNo", sitter_no);

        return "sitter/reserve";
    }
}
