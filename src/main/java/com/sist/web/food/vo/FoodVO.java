package com.sist.web.food.vo;

import lombok.Data;

@Data
public class FoodVO {
	private int id;
    private String title;
    private String price;
    private String image_url;
    private String detail_url;
    private String origin;
    private String manufacturer;
    private String expiry;
    private String age_recommendation;
    private String weight;
    private String ingredients;
    private String nutrition;
    private String contact;
    private String description;
}
