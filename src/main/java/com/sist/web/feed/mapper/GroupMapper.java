package com.sist.web.feed.mapper;
import java.util.*;

import com.sist.web.feed.vo.*;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface GroupMapper {
	@Select("SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at, num "
			+ "FROM (SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at, rownum as num "
			+ "FROM (SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at "
			+ "FROM p_group ORDER BY group_no DESC))")
	public List<GroupVO> groupListData();
	
	@Select("SELECT * FROM p_group WHERE group_no=#{group_no}")
	public GroupVO groupDetailData(int group_no);
	
	// 피드는 페이징 안할거야 -> 무한스크롤로 수정할예쩡
	@Select("SELECT feed_no,group_no,user_no,title,filecount,regdate "
			+ "FROM p_feed "
			+ "WHERE group_no=#{group_no} "
			+ "ORDER BY feed_no DESC")
	public List<FeedVO> feedListData(int group_no);
	
	@Select("SELECT * FROM p_feed_fileInfo WHERE feed_no=#{feed_no}")
	public List<FeedFileInfoVO> fileListData(int feed_no);
	
	@Insert("INSERT INTO p_feed VALUES(p_feed_seq.nextval,#{group_no},#{user_no},#{title},#{content},#{filecount},SYSDATE,SYSDATE)")
	public void feedInsertData(FeedVO vo);
	
	@Select("SELECT p_feed_seq.currval FROM DUAL")
	public int feedCurentNodata();
	
	@Insert("INSERT INTO p_feed_fileInfo VALUES(p_feed_fileinfo_seq.nextval,#{feed_no},#{filename},#{filesize})")
	public void feedFileInsert(FeedFileInfoVO vo);
	
	
	
	
}

