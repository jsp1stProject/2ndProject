package com.sist.web.facility.dao;
import java.util.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.sist.web.facility.mapper.*;
import com.sist.web.facility.vo.FacilityVO;
@Repository
public class FacilityDAO {
	@Autowired
	private FacilityMapper mapper;
	
	public List<FacilityVO> facilityList(@Param("category") String category,
            @Param("start") int start,
            @Param("end") int end){
		return mapper.facilityList(category, start, end);
	}
	
	public int facilityTotalPage(@Param("category") String category) {
		return mapper.facilityTotalPage(category);
	}
	
	public List<String> categoryList(){
		return mapper.categoryList();
	}
}
