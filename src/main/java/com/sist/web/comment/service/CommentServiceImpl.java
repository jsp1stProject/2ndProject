package com.sist.web.comment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.comment.mapper.CommentMapper;
import com.sist.web.comment.vo.CommentVO;
@Service
public class CommentServiceImpl implements CommentService{
	@Autowired
	private CommentMapper mapper;

	@Override
	public List<CommentVO> commentList(int post_id) {
		// TODO Auto-generated method stub
		return mapper.commentList(post_id);
	}

	@Override
	public void commentInsert(CommentVO vo) {
		// TODO Auto-generated method stub
		mapper.commentInsert(vo);
	}

	@Override
	public void commentDelete(int comment_id) {
		// TODO Auto-generated method stub
		mapper.commentDelete(comment_id);
	}

}
