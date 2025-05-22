package com.sist.web.sitter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.sitter.dao.SitterReviewDAO;
import com.sist.web.sitter.service.SitterService;
import com.sist.web.sitter.vo.SitterReviewVO;
import com.sist.web.sitter.vo.SitterVO;

@Service
public class SitterReviewServiceImpl implements SitterReviewService {
    
    @Autowired
    private SitterReviewDAO rDao;
    
    @Autowired
    private SitterService sitterService;

    @Override
    public void insertReview(SitterReviewVO vo) {
        // 일반 유저가 작성하는 댓글 → 바로 insert
        rDao.insertReview(vo);
    }

    @Override
    public void insertReply(SitterReviewVO vo) {
        // 원댓글 조회
        SitterReviewVO parent = rDao.selectReviewById(vo.getParent_no());
        if (parent == null) {
            throw new RuntimeException("원댓글이 존재하지 않습니다.");
        }

        // 해당 sitter의 user_no를 조회해서 작성자 권한 확인
        SitterVO sitter = sitterService.sitterDetailData(parent.getSitter_no());
        if (sitter == null) {
            throw new RuntimeException("대상 펫시터 정보가 없습니다.");
        }

        // 현재 로그인한 유저가 이 펫시터 본인인지 확인
        if (vo.getUser_no() != sitter.getUser_no()) {
            throw new SecurityException("대댓글 권한이 없습니다.");
        }

        // group_id와 sitter_no 세팅 후 insert
        vo.setGroup_id(parent.getGroup_id());           // 동일 댓글 묶음
        vo.setGroup_step(1);                            // 대댓글 구분
        vo.setSitter_no(parent.getSitter_no());         // 원댓글 대상 sitter 복사

        rDao.insertReply(vo);
    }

    @Override
    public SitterReviewVO selectReviewById(int review_no) {
        return rDao.selectReviewById(review_no);
    }

	@Override
	public void deleteReviewsBySitterNo(int sitter_no) {
		rDao.deleteReviewsBySitterNo(sitter_no);
		
	}

	@Override
	public List<SitterReviewVO> reviewList(int sitter_no) {
		// TODO Auto-generated method stub
		return rDao.reviewList(sitter_no);
	}

	@Override
	public int deleteReview(int review_no,int loginUserNo) {
		// TODO Auto-generated method stub
		SitterReviewVO vo = rDao.selectReviewById(review_no);
	    if (vo == null) throw new RuntimeException("존재하지 않는 리뷰");
	    if (vo.getUser_no() != loginUserNo) throw new SecurityException("삭제 권한 없음");
	    
		return rDao.deleteReview(review_no);
	}

	@Override
	public int updateReview(SitterReviewVO vo,int loginUserNo) {
		// TODO Auto-generated method stub
		 SitterReviewVO dbVO = rDao.selectReviewById(vo.getReview_no());
		    if (dbVO == null) throw new RuntimeException("존재하지 않음");
		    if (dbVO.getUser_no() != loginUserNo) throw new SecurityException("수정 권한 없음");
		
		return rDao.updateReview(vo);
	}
}
