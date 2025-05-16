package com.sist.web.facility.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.facility.mapper.*;
import com.sist.web.facility.vo.*;

public interface FacilityService {
	public List<FacilityVO> facilityList(String category,int start,int end);
	public int facilityTotalPage(String category);
	public List<String> categoryList();
	public List<FacilityVO> facilityListFiltered(String category, String location, int start, int end);
	public int facilityTotalPageFiltered(String category, String location);

}
