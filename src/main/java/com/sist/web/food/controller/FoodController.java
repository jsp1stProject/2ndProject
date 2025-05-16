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
    public String showCalorieCalculator() {
        return "food/calorieCalculator"; // /WEB-INF/views/food/calorieCalculator.jsp
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

        // Ȱ�� ���� �������� ���� Ű���� ����
        String ageKeyword = getAgeKeywordByActivity(animal, activity);

        List<FoodVO> foodList = (animal.equals("cat"))
                ? mapper.getRecommendedCatFood(ageKeyword)
                : mapper.getRecommendedDogFood(ageKeyword);

        model.addAttribute("der", String.format("%.2f", der));
        model.addAttribute("foodList", foodList);
        model.addAttribute("animal", animal);

        return "food/recommendResult";  // /WEB-INF/views/food/recommendResult.jsp
    }

    private String getAgeKeywordByActivity(String animal, double activity) {
        if (animal.equals("cat")) {
            if (activity <= 0.8) return "���";
            else if (activity <= 1.0) return "7���̻�";
            else if (activity <= 1.2) return "12����";
            else if (activity <= 1.4) return "1��";
            else if (activity <= 2.0) return "3����";
            else if (activity <= 2.5) return "����";
            else return "�����";
        } else {
            if (activity <= 1.0) return "���";
            else if (activity <= 1.2) return "14��";
            else if (activity <= 1.6) return "12����";
            else if (activity <= 1.8) return "10����";
            else if (activity <= 2.5) return "����";
            else if (activity <= 3.0) return "�ӽż�����";
            else return "������";
        }
    }
    
}

