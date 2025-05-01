package com.sist.web.feed.vo;

import lombok.Data;

@Data
public class FeedFileInfoVO {
   private int file_no, feed_no;
   private String filename;
   private long filesize;
}
