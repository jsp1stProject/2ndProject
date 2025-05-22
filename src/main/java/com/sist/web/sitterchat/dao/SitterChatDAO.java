package com.sist.web.sitterchat.dao;
import com.sist.web.sitterchat.vo.*;
import com.sist.web.sitterchat.mapper.*;
import java.util.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository
public class SitterChatDAO {
	@Autowired
	private SitterChatMapper mapper;
	
	public List<SitterChatRoomVO> selectChatRoomList(int user_no)
	{
		return mapper.selectChatRoomList(user_no);
	}
	public SitterChatRoomVO selectChatRoom(int user1_no, int user2_no, int reserve_no)
    {
    	return mapper.selectChatRoom(user1_no, user2_no, reserve_no);
    }
	public int insertChatRoom(SitterChatRoomVO vo)
    {
    	return mapper.insertChatRoom(vo);
    }
	public int deleteChatRoom(int room_no)
    {
    	return mapper.deleteChatRoom(room_no);
    }

	public List<SitterChatVO> selectChatList(int room_no)
    {
    	return mapper.selectChatList(room_no);
    }
	public int insertChat(SitterChatVO vo)
    {
    	return mapper.insertChat(vo);
    }
	public List<SitterChatVO> searchChatByKeyword(int room_no, String keyword)
    {
    	return mapper.searchChatByKeyword(room_no, keyword);
    }

	public String isChatEnabled(int room_no)
    {
    	return mapper.isChatEnabled(room_no);
    }
	public int selectChatRoomTotalPage()
	{
		return mapper.selectChatRoomTotalPage();
	}
	public SitterChatRoomVO SitterChatRoomById(int room_no)
	{
		return mapper.SitterChatRoomById(room_no);
	}
	
}
