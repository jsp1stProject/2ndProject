package com.sist.web.feed.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.web.feed.dao.ScheduleDAO;
import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleDAO dao;
		
	@Override
	public void groupScheduleInsert(ScheduleVO vo) {
		dao.groupScheduleInsert(vo); 
	}

	@Override
	public void scheduleMemberInsert(ScheduleMemberVO vo) {
		dao.scheduleMemberInsert(vo);
	}
	
	@Transactional
	@Override
	public String scheduleInsertData(int group_no, ScheduleVO vo, long user_no)
	{
		System.out.println("서비스임플");	
		String result="";
		try {
			vo.getSche_start_str();
		    vo.getSche_end_str();
		    
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
			if (vo.getSche_start_str() != null && !vo.getSche_start_str().isEmpty())
			{
			    LocalDateTime start = LocalDateTime.parse(vo.getSche_start_str(), formatter);
			    vo.setSche_start(Timestamp.valueOf(start));
			}

			if (vo.getSche_end_str() != null && !vo.getSche_end_str().isEmpty())
			{
			    LocalDateTime end = LocalDateTime.parse(vo.getSche_end_str(), formatter);
			    vo.setSche_end(Timestamp.valueOf(end));
			}
			vo.setUser_no(user_no);
		    System.out.println(vo);
		    groupScheduleInsert(vo);
			System.out.println("인서트1");
			List<Long> participants = new ArrayList<Long>();
			
			participants = vo.getParticipants_no();
			for(long participant : participants)
			{
				ScheduleMemberVO mvo = new ScheduleMemberVO();
				mvo.setSche_no(vo.getSche_no());
				mvo.setUser_no(participant);
				dao.scheduleMemberInsert(mvo);
				System.out.println("인서트2");
			}
			
			System.out.println("인서트3");
			result="인서트완료";
		} catch (Exception e) {
			// TODO: handle exception
			result="인서트오류";
			e.printStackTrace();
		}
		return result;
	}


	@Override
	public List<ScheduleVO> scheduleGroupList(Map map) {
		System.out.println(dao.scheduleGroupList(map));
		System.out.println(dao.scheduleGroupList(map).size());
		return dao.scheduleGroupList(map);
	}

	@Override
	public List<ScheduleVO> scheduleGroupListData(long user_no, int group_no) {
		Map map = new HashMap();
		map.put("group_no", group_no);
		map.put("user_no", user_no);
		List<ScheduleVO> list = scheduleGroupList(map);

		return list;
	}

	@Override
	public List<ScheduleVO> scheduleUserTotalList(long user_no) {
		return dao.scheduleUserTotalList(user_no);
	}

	@Override
	public void scheduleInsert(ScheduleVO vo) {
		dao.scheduleInsert(vo);
	}

	@Override
	public ScheduleVO schedule_detail(int sche_no) {
		// TODO Auto-generated method stub
		ScheduleVO vo = new ScheduleVO();
		try {
			vo = dao.schedule_detail(sche_no);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return vo;
	}

	@Override
	public List<ScheduleMemberVO> schedule_participatns(int shce_no) {
		// TODO Auto-generated method stub
		return dao.schedule_participatns(shce_no);
	}
	
	@Override
	public ScheduleVO scheduleDetailData(int shce_no) {
		ScheduleVO vo = new ScheduleVO();
		try {
			System.out.println("serviceimpl");
			vo = dao.schedule_detail(shce_no);
			System.out.println(vo);
			vo.setParticipants(dao.schedule_participatns(shce_no));
			System.out.println(vo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return vo;
	}

	@Override
	public List<ScheduleVO> schedule_selected_data(@Param("selected_date") String selected_date,
            @Param("user_no") long user_no) {
		// TODO Auto-generated method stub
		return dao.schedule_selected_data(selected_date,user_no);
	}

	@Override
	public List<ScheduleVO> schedule_dday(long user_no) {
		// TODO Auto-generated method stub
		return dao.schedule_dday(user_no);
	}

	@Override
	public List<ScheduleVO> schedule_important(long user_no) {
		// TODO Auto-generated method stub
		return dao.schedule_important(user_no);
	}

	@Override
	public void deleteSchedule(int sche_no) {
		dao.deleteSchedule(sche_no);
		
	}

	@Override
	public void deleteScheduleMember(int sche_no) {
		dao.deleteScheduleMember(sche_no);
	}
	
	@Transactional
	@Override
	public String deleteScheduleData(int sche_no, int type) {
		
		String result="";
		try {
			if(type == 1) {
				dao.deleteScheduleMember(sche_no);
			}
			dao.deleteSchedule(sche_no);
			result="일정삭제성공";
		} catch (Exception e) {
			// TODO: handle exception
			result="일정삭제실패";
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String updateScheduleData(ScheduleVO vo)
	{
		String result="";
		try {
	        //String pattern = "yyyy-MM-ddTHH:mm";
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

	        if (vo.getSche_start_str() != null && !vo.getSche_start_str().isEmpty()) {
	            vo.setSche_start(sdf.parse(vo.getSche_start_str()));
	        }
	        if (vo.getSche_end_str() != null && !vo.getSche_end_str().isEmpty()) {
	            vo.setSche_end(sdf.parse(vo.getSche_end_str()));
	        }
	        dao.updateSchedule(vo);
	        result="일정삭제성공";
	    } catch (Exception e) {
	        e.printStackTrace();
	        result="일정삭제실패";
	    }
		return result;
	}

	@Override
	public void updateSchedule(ScheduleVO vo) {
		dao.updateSchedule(vo);
		
	}

	@Override
	public List<ScheduleVO> schedulePagingUserTotalList(Map map) {
		// TODO Auto-generated method stub
		return dao.schedulePagingUserTotalList(map);
	}

	@Override
	public int scheduleUserTotalCount(long user_no) {
		// TODO Auto-generated method stub
		return dao.scheduleUserTotalCount(user_no);
	}
	
	@Override
	public Map<String, Object> schedulePagingData(int page, long user_no)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(page==0)
			{
				page=1;
			}
			int rowSize = 15;
			int start = (rowSize*page)-(rowSize-1);
			int end = rowSize*page;
			
			final int BLOCK=10;
			int startPage=((page-1)/BLOCK*BLOCK)+1;
			int endPage=((page-1)/BLOCK*BLOCK)+BLOCK;
			int totalpage=dao.scheduleUserTotalCount(user_no);   
			if(endPage>totalpage)
			   endPage=totalpage;
			System.out.println(start);
			System.out.println(end);
			System.out.println(user_no);
			
			map.put("start", start);
			map.put("end", end);
			map.put("user_no", user_no);
			
			List<ScheduleVO> list = dao.schedulePagingUserTotalList(map);
			map = new HashMap<String, Object>();
			map.put("list", list);
			map.put("curpage", page);
			map.put("totalpage", totalpage);
			map.put("startPage", startPage);
			map.put("endPage", endPage);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return map;
	}

	@Override
	public List<ScheduleVO> schedulePagingUserSearchlList(Map map) {
		// TODO Auto-generated method stub
		return dao.schedulePagingUserSearchlList(map);
	}

	@Override
	public int scheduleUserTotalCountWithSearch(long user_no, String search) {
		// TODO Auto-generated method stub
		return dao.scheduleUserTotalCountWithSearch(user_no, search);
	}

	@Override
	public Map<String, Object> schedulePagingSearchData(int page, long user_no, String search) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(page==0)
			{
				page=1;
			}
			int rowSize = 15;
			int start = (rowSize*page)-(rowSize-1);
			int end = rowSize*page;
			
			final int BLOCK=10;
			int startPage=((page-1)/BLOCK*BLOCK)+1;
			int endPage=((page-1)/BLOCK*BLOCK)+BLOCK;
			int totalpage=dao.scheduleUserTotalCountWithSearch(user_no,search);   
			if(endPage>totalpage)
			   endPage=totalpage;
			
			map.put("start", start);
			map.put("end", end);
			map.put("user_no", user_no);
			map.put("search", search);
			
			List<ScheduleVO> list = dao.schedulePagingUserSearchlList(map);
			map = new HashMap<String, Object>();
			map.put("list", list);
			map.put("curpage", page);
			map.put("totalpage", totalpage);
			map.put("startPage", startPage);
			map.put("endPage", endPage);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return map;
	}
}
