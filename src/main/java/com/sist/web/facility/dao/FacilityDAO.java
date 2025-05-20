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
	// 지역,필터 추가
	public List<FacilityVO> facilityListFiltered(@Param("category") String category,
            @Param("location") String location,
            @Param("start") int start,
            @Param("end") int end) {
	return mapper.facilityListFiltered(category, location, start, end);
	}

	public int facilityTotalPageFiltered(@Param("category") String category,
	    @Param("location") String location) {
	return mapper.facilityTotalPageFiltered(category, location);
	}
	//
	
	public int facilityTotalPage(@Param("category") String category) {
		return mapper.facilityTotalPage(category);
	}
	
	public List<String> categoryList(){
		return mapper.categoryList();
	}
	
	public List<Map<String, Object>> findNearbyFacilities(double lat, double lon){
		return mapper.findNearbyFacilities(lat, lon);
	}
	public FacilityVO facilityDetail(@Param("facilityId") int facilityId) {
		return mapper.facilityDetail(facilityId);
	}
	
}
