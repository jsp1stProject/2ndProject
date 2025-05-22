package com.sist.web.food.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/*
@Controller
public class CalorieController {

    @Autowired
    private CalorieService calorieService;

    @PostMapping("/calculate")
    public String calculate(
        @RequestParam("petType") String petType,
        @RequestParam("weight") double weight,
        @RequestParam("activity") double activity,
        Model model) {
        
        double rer = 70 * Math.pow(weight, 0.75);
        double der = rer * activity;

        List<FeedVO> recommendations = calorieService.recommendFood(petType, der);

        model.addAttribute("der", der);
        model.addAttribute("recommendations", recommendations);
        return "result";
    }
}
*/


