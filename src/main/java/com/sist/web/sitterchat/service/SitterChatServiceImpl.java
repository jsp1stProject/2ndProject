package com.sist.web.sitterchat.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.sitterchat.dao.SitterChatDAO;
import com.sist.web.sitterchat.vo.SitterChatRoomVO;
import com.sist.web.sitterchat.vo.SitterChatVO;

@Service
public class SitterChatServiceImpl implements SitterChatService{
	@Autowired
	private SitterChatDAO cDao;

	@Override
	public List<SitterChatRoomVO> SitterChatRoomList(int userNo, int start, int end) {
		// TODO Auto-generated method stub
		return cDao.SitterChatRoomList(userNo, start, end);
	}

	@Override
	public int SitterChatRoomTotalPage(int userNo, int rowSize) {
		// TODO Auto-generated method stub
		return cDao.SitterChatRoomTotalPage(userNo, rowSize);
	}

	@Override
	public int SitterChatRoomInsert(SitterChatRoomVO vo) {
		// TODO Auto-generated method stub
		return cDao.SitterChatRoomInsert(vo);
	}

	@Override
	public int SitterChatRoomDelete(int room_id) {
		// TODO Auto-generated method stub
		return cDao.SitterChatRoomDelete(room_id);
	}

	@Override
	public int SitterChatInsert(SitterChatVO vo) {
		// TODO Auto-generated method stub
		return cDao.SitterChatInsert(vo);
	}

	@Override
	public List<SitterChatVO> SitterChatList(int room_id) {
		// TODO Auto-generated method stub
		return cDao.SitterChatList(room_id);
	}

	@Override
	public List<SitterChatVO> SitterChatSearch(int room_id, String keyword) {
		// TODO Auto-generated method stub
		return cDao.SitterChatSearch(room_id, keyword);
	}

	@Override
	public List<SitterChatRoomVO> SitterChatRoomListWithFilter(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return cDao.SitterChatRoomListWithFilter(map);
	}

	@Override
	public int SitterChatRoomTotalPageWithFilter(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return cDao.SitterChatRoomTotalPageWithFilter(map);
	}
	
	
}
