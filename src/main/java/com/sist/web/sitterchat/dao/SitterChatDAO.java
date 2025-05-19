package com.sist.web.sitterchat.dao;
import com.sist.web.sitterchat.vo.*;
import com.sist.web.sitterchat.mapper.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository
public class SitterChatDAO {
	@Autowired
	private SitterChatMapper mapper;
	
	public List<SitterChatRoomVO> SitterChatRoomList(int userNo,int start,int end)
	{
		return mapper.SitterChatRoomList(userNo, start, end);
	}
	public int SitterChatRoomTotalPage(int userNo,int rowSize)
	{
		return mapper.SitterChatRoomTotalPage(userNo, rowSize);
	}
	public int SitterChatRoomInsert(SitterChatRoomVO vo)
	{
		return mapper.SitterChatRoomInsert(vo);
	}
	public int SitterChatRoomDelete(int room_id)
	{
		return mapper.SitterChatRoomDelete(room_id);
	}
	public int SitterChatInsert(SitterChatVO vo)
	{
		return mapper.SitterChatInsert(vo);
	}
	public List<SitterChatVO> SitterChatList(int room_id)
	{
		return mapper.SitterChatList(room_id);
	}
	public List<SitterChatVO> SitterChatSearch(int room_id,String keyword)
	{
		return mapper.SitterChatSearch(room_id, keyword);
	}
	public List<SitterChatRoomVO> SitterChatRoomListWithFilter(Map<String, Object> map)
	{
		return mapper.SitterChatRoomListWithFilter(map);
	}
	public int SitterChatRoomTotalPageWithFilter(Map<String, Object> map)
	{
		return mapper.SitterChatRoomTotalPageWithFilter(map);
	}
	
}
