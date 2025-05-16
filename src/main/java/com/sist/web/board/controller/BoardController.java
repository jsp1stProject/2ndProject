package com.sist.web.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;

import java.util.*;
import com.sist.web.board.*;
import com.sist.web.board.service.BoardService;
import com.sist.web.board.vo.BoardVO;
@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService service;
	@GetMapping("/list")
	public String boardList(@RequestParam(defaultValue = "1") int page,
	@RequestParam(required = false) String type,@RequestParam(required = false) String keyword,Model model) {
	int rowSize = 10;
	int start = (page - 1) * rowSize + 1;
	int end = start + rowSize - 1;
	
	Map<String, Object> map = new HashMap<>();
	map.put("start", start);
	map.put("end", end);
	if (type != null && keyword != null && !keyword.isEmpty()) {
	map.put("type", type);
	map.put("keyword", "%" + keyword + "%");
	}
	
	List<BoardVO> list = service.boardListData(map);
	int total = service.boardTotalCount();
	int totalPage = (int) Math.ceil(total / (double) rowSize);
	
	model.addAttribute("list", list);
	model.addAttribute("page", page);
	model.addAttribute("totalPage", totalPage);
	model.addAttribute("type", type);
	model.addAttribute("keyword", keyword);
	
	return "board/list";  // /WEB-INF/views/board/list.jsp
	}
	
	@GetMapping("/detail")
	public String boardDetail(@RequestParam("post_id") int post_id, Model model) {
	service.boardHit(post_id); // 조회수 증가
	BoardVO vo = service.boardDetail(post_id);
	model.addAttribute("vo", vo);
	return "board/detail";
	}

	@GetMapping("/insert")
	public String boardInsertForm() {
	return "board/insert";
	}
	
	// 게시글 작성 처리
	@PostMapping("/insert")
	public String boardInsertSubmit(BoardVO vo, HttpSession session) {
		Integer userNo = (Integer) session.getAttribute("user_no");
        if (userNo != null) {
            vo.setUser_no(userNo);  // 안전하게 다시 세션값 넣기
            service.boardInsert(vo);
        }
	return "redirect:/board/list";
	}
	
	// 게시글 수정 폼
	@GetMapping("/update")
	public String boardUpdateForm(@RequestParam("post_id") int post_id, Model model) {
	BoardVO vo = service.boardDetail(post_id);
	model.addAttribute("vo", vo);
	return "board/update";
	}
	
	// 게시글 수정 처리
	@PostMapping("/update")
	public String boardUpdateSubmit(BoardVO vo) {
	service.boardUpdate(vo);
	return "redirect:/board/detail?post_id=" + vo.getPost_id();
	}
	
	@GetMapping("/delete")
	public String boardDelete(@RequestParam("post_id") int post_id) {
	service.boardDelete(post_id);
	return "redirect:/board/list";
	}
}
