package com.sist.web.comment.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sist.web.comment.service.CommentService;
import com.sist.web.comment.vo.CommentVO;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService service;

    @GetMapping("/list")
    public List<CommentVO> commentList(@RequestParam int post_id) {
        return service.commentList(post_id);
    }

    @PostMapping("/insert")
    public void commentInsert(@RequestBody CommentVO vo) {
        service.commentInsert(vo);
    }

    @DeleteMapping("/delete")
    public void commentDelete(@RequestParam int comment_id) {
        service.commentDelete(comment_id);
    }
}

