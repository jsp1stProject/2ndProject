package com.sist.web.groupchat.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class GroupOnlineUserServiceImpl implements OnlineUserService {

	private final Set<Long> onlineUserNos = ConcurrentHashMap.newKeySet();

	@Override
    public void markOnline(long userNo) {
        onlineUserNos.add(userNo);
    }

    @Override
    public void markOffline(long userNo) {
        onlineUserNos.remove(userNo);
    }

    @Override
    public boolean isOnline(long userNo) {
        return onlineUserNos.contains(userNo);
    }

    @Override
    public Set<Long> getOnlineUsers() {
        return onlineUserNos;
    }
	

}
