package com.sist.web.groupchat.service;

import java.util.Set;

public interface OnlineUserService {
	void markOnline(long userNo);
    void markOffline(long userNo);
    boolean isOnline(long userNo);
    Set<Long> getOnlineUsers();
}
