package com.sist.web.facility.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.facility.mapper.FacilityMapper;
import com.sist.web.facility.vo.FacilityVO;

@Service
public class FacilityService2 {
	@Autowired
    private FacilityMapper facilityMapper;
	
	//public List<FacilityVO> facilityList(String category,int start,int end);
	
    public List<Map<String, Object>> findNearbyFacilities(double lat, double lon) {
        return facilityMapper.findNearbyFacilities(lat, lon);
    }
}
