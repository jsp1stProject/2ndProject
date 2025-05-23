package com.sist.web.sitter.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.sist.web.sitter.vo.SitterReviewVO;
import com.sist.web.sitter.vo.SitterVO;

public interface SitterService {
	// 목록, 상세보기
	public List<SitterVO> sitterListDataAll(Map map);
    public List<SitterVO> sitterListDataWithFilter(Map map);
    public int sitterTotalPage();
	public SitterVO sitterDetailData(int sitter_no);
	public void sitterInsert(SitterVO vo);
	public void sitterUpdate(SitterVO vo);
	public void sitterDelete(int sitter_no);
	public boolean isSitter(int user_no);
	public boolean hasSitterPost(int user_no);
	// 찜
	public List<SitterVO> jjimSitterList(int user_no);
    public boolean toggleJjim(int user_no, int sitter_no);
    public void deleteJjimAll(int sitter_no);
    
   
}