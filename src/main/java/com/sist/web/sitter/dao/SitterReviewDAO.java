package com.sist.web.sitter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sist.web.sitter.mapper.SitterReviewMapper;
import com.sist.web.sitter.vo.*;
@Repository
public class SitterReviewDAO {
	@Autowired
	private SitterReviewMapper mapper;
	
	public void insertReview(SitterReviewVO vo)
	{
		mapper.insertReview(vo);
	}
	public void insertReply(SitterReviewVO vo)
	{
		mapper.insertReply(vo);
	}
	public SitterReviewVO selectReviewById(int review_no)
	{
		return mapper.selectReviewById(review_no);
	}
	public void deleteReviewsBySitterNo(int sitter_no)
	{
		mapper.deleteReviewsBySitterNo(sitter_no);
	}
	public List<SitterReviewVO> reviewList(int sitter_no)
	{
		return mapper.reviewList(sitter_no);
	}
	public int deleteReview(int review_no)
	{
		return mapper.deleteReview(review_no);
	}
	public int updateReview(SitterReviewVO vo)
	{
		return mapper.updateReview(vo);
	}
	
}
