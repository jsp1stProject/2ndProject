package com.sist.web.sitterchat.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sist.web.sitterchat.vo.SitterChatRoomVO;
import com.sist.web.sitterchat.vo.SitterChatVO;

public interface SitterChatService {
	public List<SitterChatRoomVO> selectChatRoomList(int user_no);

    public SitterChatRoomVO selectChatRoom(int user1_no, int user2_no, int reserve_no);

    public int insertChatRoom(SitterChatRoomVO vo);

    public int deleteChatRoom(int room_no);

    public List<SitterChatVO> selectChatList(int room_no);

    public int insertChat(SitterChatVO vo);

    public List<SitterChatVO> searchChatByKeyword(int room_no, String keyword);

    public String isChatEnabled(int room_no);
    
    public int selectChatRoomTotalPage();
    
    public SitterChatRoomVO SitterChatRoomById(int room_no);
}
