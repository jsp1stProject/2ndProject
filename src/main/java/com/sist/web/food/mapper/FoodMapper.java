package com.sist.web.food.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sist.web.food.vo.FoodVO;

public interface FoodMapper {
	
	@Select("SELECT * FROM p_cat_food "
	          + "WHERE age_recommendation LIKE '%' || #{ageKeyword} || '%' "
	          + "AND ROWNUM <= 5")
	public List<FoodVO> getRecommendedCatFood(String ageKeyword);

	@Select("SELECT * FROM p_dog_food "
	          + "WHERE age_recommendation LIKE '%' || #{ageKeyword} || '%' "
	          + "AND ROWNUM <= 5")
	public List<FoodVO> getRecommendedDogFood(String ageKeyword);
}
