package com.sist.web.facility.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.facility.dao.FacilityDAO;
import com.sist.web.facility.vo.FacilityVO;

@Service
public class FacilityServiceImpl implements FacilityService{
	@Autowired
	private FacilityDAO Fdao;

	@Override
	public List<FacilityVO> facilityList(String category, int start, int end) {
		// TODO Auto-generated method stub
		return Fdao.facilityList(category, start, end);
	}

	@Override
	public int facilityTotalPage(String category) {
		// TODO Auto-generated method stub
		return Fdao.facilityTotalPage(category);
	}

	@Override
	public List<String> categoryList() {
		// TODO Auto-generated method stub
		return Fdao.categoryList();
	}

	@Override
	public List<FacilityVO> facilityListFiltered(String category, String location, int start, int end) {
		// TODO Auto-generated method stub
		return Fdao.facilityListFiltered(category, location, start, end);
	}

	@Override
	public int facilityTotalPageFiltered(String category, String location) {
		// TODO Auto-generated method stub
		return Fdao.facilityTotalPage(category);
	}

	@Override
	public List<Map<String, Object>> findNearbyFacilities(double lat, double lon) {
		// TODO Auto-generated method stub
		return Fdao.findNearbyFacilities(lat, lon);
	}

	@Override
	public FacilityVO facilityDetail(int facilityId) {
		// TODO Auto-generated method stub
		return Fdao.facilityDetail(facilityId);
	}
	

}
