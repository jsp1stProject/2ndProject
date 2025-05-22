package com.sist.web.feed.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.stream.events.Comment;

import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.feed.dao.*;
import com.sist.web.feed.vo.*;

@Service
public class GroupFeedServiceImpl implements GroupFeedService{
	
	@Autowired
	private GroupFeedDAO dao;
	
	@Override
	public List<GroupVO> groupListData() {
		// TODO Auto-generated method stub
		return dao.groupListData();
	}

	@Override
	public GroupVO groupDetailData(int group_no) {
		// TODO Auto-generated method stub
		return dao.groupDetailData(group_no);
	}
	
	@Override
	public List<FeedVO> feedListData(int group_no) {
		// TODO Auto-generated method stub
		
		
		return dao.feedListData(group_no);
	}

	@Override
	public List<FeedFileInfoVO> fileListData(int no) {
		// TODO Auto-generated method stub
		
		
		return dao.fileListData(no);
	}

	@Override
	public List<GroupMemberVO> joined_groupmember(int group_no) {
		// TODO Auto-generated method stub
		return dao.joined_groupmember(group_no);
	}
	
	@Override
	public Map groupFeedData(int group_no)
	{
		Map map = new HashMap();
		GroupVO gvo = dao.groupDetailData(group_no);
		
		List<FeedVO> feedList = dao.feedListData(group_no);
		
		for(FeedVO vo : feedList)
		{
			List<FeedFileInfoVO> flist = dao.fileListData(vo.getFeed_no());
			List<String> filenames = new ArrayList<String>();
			for(FeedFileInfoVO ffvo : flist)
			{
				filenames.add(ffvo.getFilename());
			}
			vo.setImages(filenames);	
			/*
			List<String> filenames = fvo.stream()
				    .map(FeedFileInfoVO::getFilename)
				    .collect(Collectors.toList());
			*/
			//stream으로 하면 코드는 간편한데 아직 공부못한부분
		}
		List<GroupMemberVO> mvo = joined_groupmember(group_no);
		System.out.println("mvo"+mvo);
		map.put("mvo", mvo);
		map.put("list", feedList);
		map.put("gvo", gvo);
		
		return map;
		
	}

	@Override
	public int feedInsertData(FeedVO vo) {
		// TODO Auto-generated method stub
		return dao.feedInsertData(vo);
		
	}


	@Override
	public void feedFileInsert(FeedFileInfoVO vo) {
		// TODO Auto-generated method stub
		dao.feedFileInsert(vo);
	}

	@Override
	public FeedVO feedDetailData(int feed_no) {
		// TODO Auto-generated method stub
		
		
		return dao.feedDetailData(feed_no);
	}
	
	@Override
	public FeedVO feedData(int feed_no)
	{
		FeedVO vo = dao.feedDetailData(feed_no);
		List<FeedFileInfoVO> flist = dao.fileListData(vo.getFeed_no());
		List<String> filenames = new ArrayList<String>();
		for(FeedFileInfoVO ffvo : flist)
		{
			filenames.add(ffvo.getFilename());
		}
		vo.setImages(filenames);
		System.out.println(vo.getRegdate());

		return vo;	
	}

	@Override
	public int groupInsertData(GroupVO vo) {
		// TODO Auto-generated method stub
		return dao.groupInsertData(vo);
	}
	
	@Override
	public List<FeedCommentVO> feedCommentListData(Map map) {
		// TODO Auto-generated method stub
		return dao.feedCommentListData(map);
	}	
	
	@Override
	public int feedCommentTotalPage(int feed_no) {
		// TODO Auto-generated method stub
		return 0;
	}
		/*
	@Select("SELECT no, user_no, feed_no, msg, grou_step, group_id, TO_CHAR(regdate,'YYYY-MM-DD HH24:MI:SS') as dbday, num "
			+ "FROM (SELECT no, user_no, feed_no, msg, grou_step, group_id, regdate, rownum as num "
			+ "FROM (SELECT SELECT no, user_no, feed_no, msg, grou_step, group_id, regdate "
			+ "FROM p_feed_comment WHERE feed_no={feed_no} ORDER BY group_id DESC, group_step ASC)) "
			+ "WHERE num BETWEEN #{start} AND #{end}")
	 */
	@Override
	public Map FeedCommentTotalList(int page, int feed_no)
	{
		Map map = new HashMap();
		int rowSize=10;
		map.put("start", (page-1)*rowSize);
		map.put("end", page*rowSize);
		map.put("feed_no", feed_no);
		
		List<FeedCommentVO> list = feedCommentListData(map);
		int totalpage= feedCommentTotalPage(feed_no);
		
		final int BLOCK=5;
		int startPage=((page-1)/BLOCK*BLOCK)+1;
		int endPage=((page-1)/BLOCK*BLOCK)+BLOCK;
		   
		if(endPage>totalpage)
			endPage=totalpage;
		
		map.put("list", list);
		map.put("curpage", page);
		map.put("totalpage", totalpage);
		map.put("startPage", startPage);
		map.put("endPage", endPage);
		
		return map;
	}
	
	/*
	@Insert("INSERT INTO p_feed_comment(no,user_no,feed_no,msg, group_id) VALUES(p_feedcom_seq.nextval, #{user_no}, #{feed_no}, #{msg}, #{group_id})")
	
	 */
	
	@Override
	public void feedCommentInsert(FeedCommentVO vo) {
		// TODO Auto-generated method stub
		dao.feedCommentInsert(vo);
	}
	
	@Override
	public Map feedCommentAdd(int feed_no,FeedCommentVO vo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		long user_no = (long)request.getAttribute("userno");
		vo.setUser_no(user_no);
		dao.feedCommentInsert(vo);
		
		map=FeedCommentTotalList(1, feed_no);
		
		return map;
	}

	@Override
	public void feedCommentUpdate(String msg, int no) {
		// TODO Auto-generated method stub
		dao.feedCommentUpdate(msg, no);
	}
	
	@Override
	public Map feedCommentUpdateData(FeedCommentVO vo) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		dao.feedCommentUpdate(vo.getMsg(), vo.getNo());
		
		map=FeedCommentTotalList(1, vo.getFeed_no());
		
		return map;
	}

	@Override
	public void feedCommentDelete(Map map) {
		// TODO Auto-generated method stub
		dao.feedCommentDelete(map);
	}
	/*
	<select id="feedCommentDelete" parameterType="hashmap">
	   DELETE FROM p_feed_comment WHERE 
	   <if test="group_step==0">
	    group_id=#{group_id}
	   </if>
	   <if test="group_step!=0">
	    no=#{no}
	   </if>
	  </select>
	 */
	@Override 
	public Map feedCommentDeleteData(FeedCommentVO vo)
	{
		Map map = new HashedMap();
		map.put("group_id", vo.getGroup_id());
		map.put("no", vo.getNo());
		
		dao.feedCommentDelete(map);
		
		map = FeedCommentTotalList(1, vo.getFeed_no());
		
		return map;
	}



	




	

}
