package com.sist.web.facility.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sist.web.facility.mapper.FacilityMapper;
import com.sist.web.facility.service.FacilityService;
import com.sist.web.facility.service.FacilityService2;
import com.sist.web.facility.vo.FacilityVO;
@Controller
@RequestMapping("/facility")
public class FacilityController {
	@Autowired
    private FacilityService service;
	//private FacilityMapper mapper;
	
	@GetMapping("/list")
	public String facilityList(
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(defaultValue = "반려의료") String category,
	    @RequestParam(defaultValue = "") String location, 
	    Model model
	) {
	    int rowSize = 20;
	    int start = (page - 1) * rowSize + 1;
	    int end = page * rowSize;

	    List<FacilityVO> list = service.facilityListFiltered(category, location, start, end);
	    int totalpage = service.facilityTotalPageFiltered(category, location); 

	    final int BLOCK = 10;
	    int startPage = ((page - 1) / BLOCK) * BLOCK + 1;
	    int endPage = startPage + BLOCK - 1;
	    if (endPage > totalpage) endPage = totalpage;

	    List<String> categoryList = service.categoryList();

	    model.addAttribute("list", list);
	    model.addAttribute("page", page);
	    model.addAttribute("totalPage", totalpage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
	    model.addAttribute("category", category);
	    model.addAttribute("location", location);
	    model.addAttribute("categoryList", categoryList);

	    return "facility/list";
	}

	@GetMapping("/detail")
	public String facilityDetail(@RequestParam("facilityId") int facilityId, Model model) {
	    FacilityVO vo = service.facilityDetail(facilityId);
	    model.addAttribute("vo", vo);
	    return "facility/detail";
	}

	
	
	// 이거 근처 시설 출력
    @RequestMapping(value = "/nearbyFacilities", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> findNearbyFacilities(@RequestParam("lat") double lat, @RequestParam("lon") double lon) {
        return service.findNearbyFacilities(lat, lon);
    }

    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String showMapPage() {
        return "facility/facility";  // JSP 뷰 이름 (예: /WEB-INF/views/map.jsp)
    }
}
