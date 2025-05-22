package com.sist.web.comment.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CommentVO {
	private int comment_id;
    private int post_id;
    private long user_no;
    private String content;
    private Date created_at;
    private String nickname;
}
