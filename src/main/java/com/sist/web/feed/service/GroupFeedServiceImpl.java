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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sist.web.aws.AwsS3Service;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.feed.dao.*;
import com.sist.web.feed.vo.*;
import com.sist.web.group.dao.GroupDAO;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupMemberDTO;

@Service
public class GroupFeedServiceImpl implements GroupFeedService{
	
	@Autowired
	private GroupFeedDAO dao;
	
	@Autowired
	private GroupDAO gdao;
	
	@Autowired
	private AwsS3Service service;
	
	@Value("${aws.url}")
	private String awsUrl;
	/*
	 * @Override public List<GroupVO> groupListData() { // TODO Auto-generated
	 * method stub return dao.groupListData(); }
	 * 
	 * @Override public GroupVO groupDetailData(int group_no) { // TODO
	 * Auto-generated method stub return dao.groupDetailData(group_no); }
	 */
	@Override
	public List<FeedVO> feedListData(Map map) {
		// TODO Auto-generated method stub
		//group_no, start, end
		
		return dao.feedListData(map);
	}

	@Override
	public List<FeedFileInfoVO> fileListData(int no) {
		// TODO Auto-generated method stub
		
		
		return dao.fileListData(no);
	}

	
	@Override
	public Map<String, Object> groupFeedTotalData(int group_no, int page, long user_no)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			GroupDTO gvo = gdao.selectGroupDetailTotal(group_no);
			gvo.setTags(gdao.selectGroupTagsByGroupNo(group_no));
			int rowSize=5;
			map.put("user_no", user_no);
			map.put("group_no", group_no);
			map.put("start", (page-1)*rowSize);
			map.put("end", page*rowSize);
			
			
			List<FeedVO> feedList = dao.feedListData(map);
			
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
			List<GroupMemberDTO> mvo = gdao.selectGroupMemberAllByGroupNo(group_no);
			System.out.println("mvo"+mvo);
			map = new HashMap<String, Object>();
			map.put("mvo", mvo);
			map.put("list", feedList);
			map.put("gvo", gvo);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}		
		
		return map;
		
	}

	@Override
	public int feedInsertData(FeedVO vo) {
		// TODO Auto-generated method stub
		return dao.feedInsertData(vo);
		
	}
	
	@Override
	public String feedInserDataTotal(int group_no, long user_no, String title, String content, List<MultipartFile> files)
	{
		FeedVO vo = new FeedVO();
		System.out.println("입력된 title은 ="+title);
		System.out.println("입력된 content은 ="+content);
		System.out.println("입력된 files는 ="+files);
		System.out.println("입력된 group_no는 "+group_no);
		
		int fileCount = (files == null || files.isEmpty()) ? 0 : files.size();
		vo.setTitle(title);
        vo.setContent(content);
        vo.setFilecount(fileCount);
        vo.setGroup_no(group_no);    
        vo.setUser_no(user_no);   
		String path="c:\\download\\";
		
		int no=dao.feedInsertData(vo);
		System.out.println("입력된 새글의 번호"+no);
			
		if(files != null && !files.isEmpty())
		{
			for (MultipartFile file : files) {
			    try {
			    	String s3Path = service.uploadFile(file, "feeds/");
			    	String fullUrl = awsUrl + s3Path;
			        FeedFileInfoVO fileVO = new FeedFileInfoVO();
			        fileVO.setFeed_no(no);
			        fileVO.setFilename(fullUrl);
			        fileVO.setFilesize(file.getSize());
			        dao.feedFileInsert(fileVO);
			    } catch (Exception e) {
			        
			        // 실패한 파일만 스킵하거나 전체 트랜잭션 롤백 등 판단 필요
			        throw new RuntimeException("파일 업로드 실패");
			    }
			}
		}
		return "파일 업로드 완료";
	}
	@Override
	public void feedFileInsert(FeedFileInfoVO vo) {
		// TODO Auto-generated method stub
		dao.feedFileInsert(vo);
	}

	@Override
	public FeedVO feedDetailData(int feed_no, long user_no) {
		// TODO Auto-generated method stub
		
		
		return dao.feedDetailData(feed_no,user_no);
	}
	
	@Override
	public FeedVO feedData(int feed_no, long user_no)
	{
		FeedVO vo = dao.feedDetailData(feed_no, user_no);
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
	public void feedUpdate(FeedVO vo) {
		dao.feedUpdate(vo);
	}
	
	@Transactional
	@Override
	public void feedDelete(int feed_no) {
		dao.feedLikeTableDelete(feed_no);
		dao.feedCommentTableDelete(feed_no);
		dao.feedFileInfoTableDelete(feed_no);
		dao.feedDelete(feed_no);
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

	@Override
	public Map FeedCommentTotalList(int page, int feed_no)
	{
		Map map = new HashMap();
		int rowSize=5;
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
	
	@Override 
	public Map feedCommentDeleteData(FeedCommentVO vo)
	{
		
		Map map = new HashedMap();
		try {
			System.out.println("vo는 "+vo);
			map.put("group_id", vo.getGroup_id());
			map.put("no", vo.getNo());
			System.out.println(map);
			dao.feedCommentDelete(map);
			System.out.println("");
			map = FeedCommentTotalList(1, vo.getFeed_no());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return map;
	}

	@Override
	public void feedReplyInsert(FeedCommentVO vo) {
		// TODO Auto-generated method stub
		dao.feedReplyInsert(vo);
	}

	@Override
	public int hasUserLike(long user_no, int feed_no) {
		// TODO Auto-generated method stub
		return dao.hasUserLike(user_no, feed_no);
	}

	@Override
	public void likeInsert(long user_no, int feed_no) {
		// TODO Auto-generated method stub
		dao.likeInsert(user_no, feed_no);
	}

	@Override
	public void likeDelete(long user_no, int feed_no) {
		// TODO Auto-generated method stub
		dao.likeDelete(user_no, feed_no);
	}

	@Override
	public boolean  selectLike(long user_no, int feed_no)
	{
		
		if (dao.hasUserLike(user_no, feed_no) > 0) {
	        dao.likeDelete(user_no, feed_no);
	        return false; // ❤️ 삭제됨 → 좋아요 안한 상태
	    } else {
	        dao.likeInsert(user_no, feed_no);
	        return true;  // ❤️ 추가됨 → 좋아요 상태
	    }
	}

	


	




	

}
