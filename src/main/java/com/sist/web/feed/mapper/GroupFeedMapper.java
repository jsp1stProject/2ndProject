package com.sist.web.feed.mapper;
import java.util.*;

import javax.xml.stream.events.Comment;

import com.sist.web.feed.vo.*;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface GroupFeedMapper {
	//그룹
/*
	@Select("SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at, num "
			+ "FROM (SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at, rownum as num "
			+ "FROM (SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at "
			+ "FROM p_group ORDER BY group_no DESC))")
	public List<GroupVO> groupListData();
	
	@Insert("INSERT INTO p_group VALUES(p_group_no_seq.nextval,#{group_name},#{profile_img},#{description},#{capacity},#{is_public},#{owner},current_timestamp)")
	public void groupInsertData(GroupVO vo);
	
	@Select("SELECT p_group_no_seq.currval FROM DUAL")
	public int groupCurentNodata();

	*/
	//피드
	// 피드는 페이징 안할거야 -> 무한스크롤로 수정할예정 -> 무한스크롤을 페이징으로 구현할 수 있어 
	
	public List<FeedVO> feedListData(Map map);
	
	@Select("SELECT CEIL(COUNT(*)/5.0) FROM p_feed WHERE group_no=#{group_no}")
	public int feedTotalCount(int group_no);
	
	@Select("SELECT * FROM p_feed_fileInfo WHERE feed_no=#{feed_no}")
	public List<FeedFileInfoVO> fileListData(int feed_no);
	/*
	 group_mapper에 있음
	@Select("SELECT user_no, nickname, role FROM p_group_member WHERE group_no=#{group_no}")
	public List<GroupMemberVO> joined_groupmember(int group_no);
	*/
	@Insert("INSERT INTO p_feed VALUES(p_feed_seq.nextval,#{group_no},#{user_no},#{title},#{content},#{filecount},SYSDATE,SYSDATE)")
	public void feedInsertData(FeedVO vo);
	
	@Select("SELECT p_feed_seq.currval FROM DUAL")
	public int feedCurentNodata();
	
	@Insert("INSERT INTO p_feed_fileInfo VALUES(p_feed_fileinfo_seq.nextval,#{feed_no},#{filename},#{filesize})")
	public void feedFileInsert(FeedFileInfoVO vo);	
	
	@Select("SELECT f.feed_no, f.group_no, f.user_no, gm.nickname,"
			+ "(SELECT u.profile FROM p_users u WHERE u.user_no = f.user_no) as profile,"
			+ "f.title, f.content, f.filecount,"
			+ "TO_CHAR(f.regdate,'YYYY-MM-DD') as dbday, f.update_time, "
			+ "(SELECT COUNT(*) FROM p_feed_comment c WHERE c.feed_no = f.feed_no) AS comment_count, "
			+ "(SELECT COUNT(*) FROM p_feed_like l WHERE l.feed_no = f.feed_no) AS like_count ,"
			+ "(SELECT COUNT(*) FROM p_feed_like l WHERE l.feed_no = f.feed_no AND l.user_no = #{user_no}) AS is_liked "
			+ "FROM p_feed f "
			+ "JOIN p_group_member gm "
			+ "ON f.user_no=gm.user_no AND f.group_no=gm.group_no "
			+ "WHERE f.feed_no=#{feed_no}")
	public FeedVO feedDetailData(@Param("feed_no") int feed_no, @Param("user_no") long user_no);
	
	//피드-댓글
	public List<FeedCommentVO> feedCommentListData(Map map);
	
	@Select("SELECT CEIL(COUNT(*)/10.0) FROM p_feed_comment WHERE feed_no={feed_no}")
	public int feedCommentTotalPage(int feed_no);
	
	@Insert("INSERT INTO p_feed_comment(no,user_no,feed_no,msg, group_id) VALUES(p_feedcom_seq.nextval, #{user_no}, #{feed_no}, #{msg}, (SELECT NVL(MAX(group_id)+1,1) FROM p_feed_comment))")
	public void feedCommentInsert(FeedCommentVO vo);
	
	@Update("UPDATE p_feed_comment SET msg=#{msg} WHERE no=#{no}")
	public void feedCommentUpdate(@Param("msg") String msg, @Param("no") int no);
	
	public void feedCommentDelete(Map map);
	
	//피드-대댓글
	@Insert("INSERT INTO p_feed_comment(no,user_no,feed_no,msg,group_id,group_step) "
			+ "VALUES(p_feedcom_seq.nextval,#{user_no}, #{feed_no}, #{msg},#{group_id},1)")
	public void feedReplyInsert	(FeedCommentVO vo);
	
	//좋아요
	@Select("SELECT COUNT(*) FROM p_feed_like WHERE user_no = #{user_no} AND feed_no = #{feed_no}")
	public int hasUserLike(@Param("user_no") long user_no, @Param("feed_no") int feed_no);
	
	@Insert("INSERT INTO p_feed_like(user_no, feed_no) VALUES(#{user_no}, #{feed_no})")
	public void likeInsert(@Param("user_no") long user_no, @Param("feed_no") int feed_no);
	
	@Delete("DELETE FROM p_feed_like WHERE user_no = #{user_no} AND feed_no = #{feed_no}")
	public void likeDelete(@Param("user_no") long user_no, @Param("feed_no") int feed_no);
}

