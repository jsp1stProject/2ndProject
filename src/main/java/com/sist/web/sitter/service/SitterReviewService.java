package com.sist.web.sitter.service;

import java.util.List;

import com.sist.web.sitter.vo.SitterReviewVO;

public interface SitterReviewService {
	public void insertReview(SitterReviewVO vo);
	public void insertReply(SitterReviewVO vo);
	public SitterReviewVO selectReviewById(int review_no);
	public void deleteReviewsBySitterNo(int sitter_no);
	public List<SitterReviewVO> reviewList(int sitter_no);
	public int deleteReview(int review_no,int loginUserNo);
	public int updateReview(SitterReviewVO vo,int loginUserNo);
}
