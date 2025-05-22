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
    public List<SitterChatRoomVO> selectChatRoomList(int user_no) {
        return cDao.selectChatRoomList(user_no);
    }

    @Override
    public SitterChatRoomVO selectChatRoom(int user1_no, int user2_no, int reserve_no) {
        return cDao.selectChatRoom(user1_no, user2_no, reserve_no);
    }

    @Override
    public int insertChatRoom(SitterChatRoomVO vo) {
        return cDao.insertChatRoom(vo);
    }

    @Override
    public int deleteChatRoom(int room_no) {
        return cDao.deleteChatRoom(room_no);
    }

    @Override
    public List<SitterChatVO> selectChatList(int room_no) {
        return cDao.selectChatList(room_no);
    }

    @Override
    public int insertChat(SitterChatVO vo) {
        return cDao.insertChat(vo);
    }

    @Override
    public List<SitterChatVO> searchChatByKeyword(int room_no, String keyword) {
        return cDao.searchChatByKeyword(room_no, keyword);
    }

    @Override
    public String isChatEnabled(int room_no) {
        return cDao.isChatEnabled(room_no);
    }

	@Override
	public int selectChatRoomTotalPage() {
		// TODO Auto-generated method stub
		return cDao.selectChatRoomTotalPage();
	}

	@Override
	public SitterChatRoomVO SitterChatRoomById(int room_no) {
		// TODO Auto-generated method stub
		return cDao.SitterChatRoomById(room_no);
	}

	
	
}
