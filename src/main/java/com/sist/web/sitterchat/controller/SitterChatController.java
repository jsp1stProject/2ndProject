package com.sist.web.sitterchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SitterChatController {

    // 채팅방 목록
    @GetMapping("/sitterchat/list")
    public String sitterChatList() {
        return "sitterchat/list"; 
    }

    // 채팅방 상세
    @GetMapping("/sitterchat/room")
    public String sitterChatRoomDetail() {
        return "sitterchat/room";  
    }
}
