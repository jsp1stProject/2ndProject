package com.sist.web.groupchat.service;

import java.util.List;
import java.util.Set;

import com.sist.web.groupchat.dto.UserStatusDTO;

public interface GroupOnlineUserService {
    void markOnline(String sessionId, long groupNo, long userNo, String nickname);
    void markOffline(String sessionId);
    List<UserStatusDTO> getOnlineUsersWithNickname(long groupNo);
    Set<Long> getGloballyOnlineUsers();
}
