package com.sist.web.feed.mapper;
import java.util.*;

import javax.xml.stream.events.Comment;

import com.sist.web.feed.vo.*;

import lombok.Delegate;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface GroupFeedMapper {
	//그룹
	@Select("SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at, num "
			+ "FROM (SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at, rownum as num "
			+ "FROM (SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at "
			+ "FROM p_group ORDER BY group_no DESC))")
	public List<GroupVO> groupListData();
	
	@Select("SELECT * FROM p_group WHERE group_no=#{group_no}")
	public GroupVO groupDetailData(int group_no);
	
	@Insert("INSERT INTO p_group VALUES(p_group_no_seq.nextval,#{group_name},#{profile_img},#{description},#{capacity},#{is_public},#{owner},current_timestamp)")
	public void groupInsertData(GroupVO vo);
	
	@Select("SELECT p_group_no_seq.currval FROM DUAL")
	public int groupCurentNodata();
	
	
	//피드
	// 피드는 페이징 안할거야 -> 무한스크롤로 수정할예정
	@Select("SELECT feed_no,group_no,user_no,title,filecount,TO_CHAR(regdate,'YYYY-mm-DD') as dbday "
			+ "FROM p_feed "
			+ "WHERE group_no=#{group_no} "
			+ "ORDER BY feed_no DESC")
	public List<FeedVO> feedListData(int group_no);
	
	@Select("SELECT * FROM p_feed_fileInfo WHERE feed_no=#{feed_no}")
	public List<FeedFileInfoVO> fileListData(int feed_no);
	
	@Select("SELECT user_no, nickname, role, status FROM p_group_member WHERE group_no=#{group_no}")
	public List<GroupMemberVO> joined_groupmember(int group_no);
	
	@Insert("INSERT INTO p_feed VALUES(p_feed_seq.nextval,#{group_no},#{user_no},#{title},#{content},#{filecount},SYSDATE,SYSDATE)")
	public void feedInsertData(FeedVO vo);
	
	@Select("SELECT p_feed_seq.currval FROM DUAL")
	public int feedCurentNodata();
	
	@Insert("INSERT INTO p_feed_fileInfo VALUES(p_feed_fileinfo_seq.nextval,#{feed_no},#{filename},#{filesize})")
	public void feedFileInsert(FeedFileInfoVO vo);	
	
	@Select("SELECT feed_no, group_no, user_no, title, content, filecount,TO_CHAR(regdate,'YYYY-mm-DD') as dbday, update_time  FROM p_feed WHERE feed_no=#{feed_no}")
	public FeedVO feedDetailData(int feed_no);
	
	//피드-댓글
	@Select("SELECT no, user_no, feed_no, msg, group_step, group_id, TO_CHAR(regdate,'YYYY-MM-DD HH24:MI:SS') as dbday, num "
			+ "FROM (SELECT no, user_no, feed_no, msg, group_step, group_id, regdate, rownum as num "
			+ "FROM (SELECT no, user_no, feed_no, msg, group_step, group_id, regdate "
			+ "FROM p_feed_comment WHERE feed_no=#{feed_no} ORDER BY group_id DESC, group_step ASC)) "
			+ "WHERE num BETWEEN #{start} AND #{end}")
	public List<FeedCommentVO> feedCommentListData(Map map);
	
	@Select("SELECT CEIL(COUNT(*)/10.0) FROM p_feed_comment WHERE feed_no={feed_no}")
	public int feedCommentTotalPage(int feed_no);
	
	@Insert("INSERT INTO p_feed_comment(no,user_no,feed_no,msg, group_id) VALUES(p_feedcom_seq.nextval, #{user_no}, #{feed_no}, #{msg}, (SELECT NVL(MAX(group_id)+1,1) FROM p_feed_comment))")
	public void feedCommentInsert(FeedCommentVO vo);
	
	@Update("UPDATE p_feed_comment SET msg=#{msg} WHERE no=#{no}")
	public void feedCommentUpdate(@Param("msg") String msg, @Param("no") int no);
	
	public void feedCommentDelete(Map map);

}

