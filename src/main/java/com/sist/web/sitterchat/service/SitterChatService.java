package com.sist.web.sitterchat.service;

import java.util.List;
import java.util.Map;

import com.sist.web.sitterchat.vo.SitterChatRoomVO;
import com.sist.web.sitterchat.vo.SitterChatVO;

public interface SitterChatService {
	public List<SitterChatRoomVO> SitterChatRoomList(int userNo,int start,int end);
	public int SitterChatRoomTotalPage(int userNo,int rowSize);
	public int SitterChatRoomInsert(SitterChatRoomVO vo);
	public int SitterChatRoomDelete(int room_id);
	public int SitterChatInsert(SitterChatVO vo);
	public List<SitterChatVO> SitterChatList(int room_id);
	public List<SitterChatVO> SitterChatSearch(int room_id,String keyword);
	public List<SitterChatRoomVO> SitterChatRoomListWithFilter(Map<String, Object> map);
	public int SitterChatRoomTotalPageWithFilter(Map<String, Object> map);
}
