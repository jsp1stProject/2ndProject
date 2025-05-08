// OnlineUserController.java
package com.sist.web.groupchat.controller;

import com.sist.web.common.response.ApiResponse;
import com.sist.web.groupchat.dto.UserStatusDTO;
import com.sist.web.groupchat.service.GroupOnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class OnlineUserController {

    private final GroupOnlineUserService groupOnlineUserService;

    @GetMapping("/{groupNo}/online")
    public ResponseEntity<ApiResponse<List<UserStatusDTO>>> getOnlineUsersByGroup(@PathVariable Long groupNo) {
        List<UserStatusDTO> onlineUsers = groupOnlineUserService.getOnlineUsersWithNickname(groupNo);
        log.info("현재 온라인 (group={}): {}", groupNo, onlineUsers);
        return ResponseEntity.ok(ApiResponse.success(onlineUsers));
    }
}
