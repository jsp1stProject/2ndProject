package com.sist.web.food.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sist.web.food.mapper.FoodMapper;
import com.sist.web.food.vo.FoodVO;

@Controller
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private FoodMapper mapper;
    
    @GetMapping("/calorieCalculator")
    public String showCalorieCalculator(Model model) {
    	model.addAttribute("main_jsp", "../food/calorieCalculator.jsp");
	    return "main/main";
    }


    @PostMapping("/recommend")
    public String recommendFood(
            @RequestParam String animal,
            @RequestParam double weight,
            @RequestParam double activity,
            Model model) {

        // RER, DER ���
        double rer = 70 * Math.pow(weight, 0.75);
        double der = rer * activity;

        
        String ageKeyword = getAgeKeywordByActivity(animal, activity);

        List<FoodVO> foodList = (animal.equals("cat"))
                ? mapper.getRecommendedCatFood(ageKeyword)
                : mapper.getRecommendedDogFood(ageKeyword);

        model.addAttribute("der", String.format("%.2f", der));
        model.addAttribute("foodList", foodList);
        model.addAttribute("animal", animal);
        model.addAttribute("main_jsp", "../food/recommendResult.jsp");

        return "main/main";  // /WEB-INF/views/food/recommendResult.jsp
    }

    private String getAgeKeywordByActivity(String animal, double activity) {
        if (animal.equals("cat")) {
            if (activity <= 0.8) return "모든";
            else if (activity <= 1.0) return "7세이상";
            else if (activity <= 1.2) return "12개월";
            else if (activity <= 1.4) return "1세";
            else if (activity <= 2.0) return "3개월";
            else if (activity <= 2.5) return "까지";
            else return "고양이";
        } else {
            if (activity <= 1.0) return "모든";
            else if (activity <= 1.2) return "14세";
            else if (activity <= 1.6) return "12개월";
            else if (activity <= 1.8) return "10개월";
            else if (activity <= 2.5) return "개월";
            else if (activity <= 3.0) return "임신수유기";
            else return "강아지";
        }
    }
    
}

