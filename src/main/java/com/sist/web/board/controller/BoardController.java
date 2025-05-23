package com.sist.web.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.*;
//import com.sist.web.board.*;
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
	model.addAttribute("main_jsp", "../board/list.jsp");
	
	return "main/main";  // /WEB-INF/views/board/list.jsp
	}
	
	@GetMapping("/detail")
	public String boardDetail(@RequestParam("post_id") int post_id, Model model) {
	service.boardHit(post_id); //
	BoardVO vo = service.boardDetail(post_id);
	model.addAttribute("vo", vo);
	model.addAttribute("main_jsp", "../board/detail.jsp");
	
	return "main/main";
	}

	@GetMapping("/insert")
	public String boardInsertForm(Model model) {
	model.addAttribute("main_jsp", "../board/insert.jsp");
    return "main/main";
	}
	
	// �Խñ� �ۼ� ó��
	@PostMapping("/insert")
	public String boardInsertSubmit(BoardVO vo, HttpServletRequest request) {
	    Object userNoObj = request.getAttribute("userno");
	    Long userNo = null;

	    if (userNoObj instanceof Long) {
	        userNo = (Long) userNoObj;
	    } else if (userNoObj instanceof Integer) {
	        userNo = ((Integer) userNoObj).longValue(); // 혹시 Integer로 들어온 경우
	    }

	    if (userNo != null) {
	        vo.setUser_no(userNo);
	        service.boardInsert(vo);
	    } else {
	        return "redirect:/login"; // 또는 오류 처리 페이지
	    }

	    return "redirect:/board/list";
	}

	
	@GetMapping("/update")
	public String boardUpdateForm(@RequestParam("post_id") int post_id, HttpServletRequest request, Model model) {
	    BoardVO vo = service.boardDetail(post_id);
	    Long userNo = (Long) request.getAttribute("userno");

	    if (userNo == null || vo == null || vo.getUser_no() != userNo) {
	        return "redirect:/board/list"; // 권한 없거나 게시글 없으면 목록으로
	    }

	    model.addAttribute("vo", vo);
	    model.addAttribute("main_jsp", "../board/update.jsp");
	    return "main/main"; 
	}

	@PostMapping("/update")
	public String boardUpdateSubmit(BoardVO vo, HttpServletRequest request, RedirectAttributes ra) {
	    Long userNo = (Long) request.getAttribute("userno");

	    BoardVO origin = service.boardDetail(vo.getPost_id());

	    if (userNo == null || origin == null || origin.getUser_no() != userNo) {
	        ra.addFlashAttribute("error", "수정 권한이 없습니다.");
	        return "redirect:/board/detail?post_id=" + vo.getPost_id();
	    }

	    vo.setUser_no(userNo); 
	    service.boardUpdate(vo);
	    return "redirect:../detail?post_id=" + vo.getPost_id();
	}
	
	@GetMapping("/delete")
	public String boardDelete(@RequestParam("post_id") int post_id,
	                          HttpServletRequest request,
	                          RedirectAttributes ra) {
	    Long userNo = (Long) request.getAttribute("userno");
	    BoardVO vo = service.boardDetail(post_id);
	    
	    if (vo == null || userNo == null || vo.getUser_no() != userNo) {
	        ra.addFlashAttribute("error", "삭제 권한이 없습니다.");
	        return "redirect:/board/detail?post_id=" + post_id;
	    }

	    service.boardDelete(post_id);
	    return "redirect:/board/list";
	}


}
