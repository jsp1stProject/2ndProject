package com.sist.web.sitter.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sist.web.sitter.mapper.SitterMapper;
import com.sist.web.sitter.vo.*;
@Repository
public class SitterDAO {
	@Autowired
	private SitterMapper mapper;
	// 펫시터 목록
	public List<SitterVO> sitterListDataAll(Map map)
	{
		return mapper.sitterListDataAll(map);
	}
	public List<SitterVO> sitterListDataWithFilter(Map map)
	{
		return mapper.sitterListDataWithFilter(map);
	}
	public int sitterTotalPage()
	{
		return mapper.sitterTotalPage();
	}
	
	// 찜하기
	public List<SitterVO> jjimSitterList(int user_no)
	{
		return mapper.jjimSitterList(user_no);
	}
	public boolean toggleJjim(int user_no, int sitter_no) {
	    if (mapper.checkJjim(user_no, sitter_no) > 0) {
	        mapper.deleteJjim(user_no, sitter_no);
	        mapper.updateJjimCount(sitter_no, -1);
	        return false;
	    } else {
	        mapper.insertJjim(user_no, sitter_no);
	        mapper.updateJjimCount(sitter_no, 1);
	        return true;
	    }
	}
	public void deleteJjimAll(int sitter_no)
	{
		mapper.deleteJjimAll(sitter_no);
	}
	
	// 상세보기
	public SitterVO sitterDetailData(int sitter_no)
	{
		return mapper.sitterDetailData(sitter_no);
	}
	public void sitterInsert(SitterVO vo)
	{
		mapper.sitterInsert(vo);
	}
	public void sitterUpdate(SitterVO vo)
	{
		mapper.sitterUpdate(vo);
	}
	public void sitterDelete(int sitter_no)
	{
		mapper.sitterDelete(sitter_no);
	}
	public int isSitter(int user_no)
	{
		return mapper.isSitter(user_no);
	}
	public int hasSitterPost(int user_no)
	{
		return mapper.hasSitterPost(user_no);
	}
}
