package com.sist.web.sitterchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.sist.web.security.*;
import com.sist.web.sitter.service.SitterResService;
import com.sist.web.sitter.vo.SitterResVO;
import com.sist.web.sitterchat.service.SitterChatService;
import com.sist.web.sitterchat.vo.SitterChatRoomVO;

@Controller
@RequestMapping("/sitterchat")
public class SitterChatController {
	 @Autowired
	    private SitterChatService sitterChatService;

	    @Autowired
	    private SitterResService sitterResService;

	    @Autowired
	    private JwtTokenProvider jwtTokenProvider;

	    @GetMapping("/chat")
	    public String chatRoomPage(@RequestParam("reserve_no") int reserve_no,
	                               @CookieValue(name = "accessToken", required = false) String token,
	                               Model model) {
	    	System.out.println("chat 시작");
	        int user_no;
	        try {
	            user_no = Integer.parseInt(jwtTokenProvider.getUserNoFromToken(token));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "redirect:/user/login";
	        }

	        // 예약 정보에서 상대방 유저(sitter) 가져오기
	        SitterResVO reserve = sitterResService.sitterReservation(reserve_no);
	        int sitter_no = reserve.getSitter_no();

	        int sitterUserNo = sitterResService.getSitterUserNoBySitterNo(sitter_no);
	        int user1 = Math.min(user_no, sitterUserNo);
	        int user2 = Math.max(user_no, sitterUserNo);
	        System.out.println("user"+user1);
	        System.out.println("user2"+user2);
	        // 기존 채팅방 존재 여부 확인
	        SitterChatRoomVO room = sitterChatService.selectChatRoom(user1, user2, reserve_no);

	        // 없으면 새로 생성
	        if (room == null) {
	            SitterChatRoomVO newRoom = new SitterChatRoomVO();
	            newRoom.setUser1_no(user1);
	            newRoom.setUser2_no(user2);
	            newRoom.setReserve_no(reserve_no);
	            sitterChatService.insertChatRoom(newRoom);

	            // 다시 조회해서 room_no 확보
	            room = sitterChatService.selectChatRoom(user1, user2, reserve_no);
	        }

	        // JSP에서 사용할 정보 전달
	        //model.addAttribute("room_no", room.getRoom_no());
	        //model.addAttribute("reserve_no", reserve_no);

	        return "sitterchat/chat";
	    }

}
