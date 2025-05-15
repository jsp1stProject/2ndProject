package com.sist.web.sitter.service;

import java.util.List;
import java.util.Map;

import com.sist.web.sitter.vo.SitterReviewVO;
import com.sist.web.sitter.vo.SitterVO;

public interface SitterService {
	public List<SitterVO> sitterListDataAll(Map map);
    public List<SitterVO> sitterListDataWithFilter(Map map);
    public int sitterTotalPage();
	public SitterVO sitterDetailData(int sitter_no);
	public void sitterInsert(SitterVO vo);
	public void sitterUpdate(SitterVO vo);
	public void sitterDelete(int sitter_no);
	public boolean isSitter(int user_no);
	public boolean hasSitterPost(int user_no);
	
	public List<SitterReviewVO> reviewListData(int sitter_no);
	public void reviewInsert(SitterReviewVO vo);
	public void replyInsert(SitterReviewVO vo);
	public void reviewUpdate(SitterReviewVO vo);
	public void reviewDelete(int review_no);
	public void deleteSitterReviewWithPost(int sitter_no);
}
