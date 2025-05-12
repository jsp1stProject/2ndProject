package com.sist.web.sitter.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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
	
	// 리뷰
	public List<SitterReviewVO> reviewListData(int sitter_no)
	{
		return mapper.reviewListData(sitter_no);
	}
	public void reviewInsert(SitterReviewVO vo)
	{
		mapper.reviewInsert(vo);
	}
	public void replyInsert(SitterReviewVO vo)
	{
		mapper.replyInsert(vo);
	}
	public void reviewDelete(int review_no)
	{
		mapper.reviewDelete(review_no);
	}
}
