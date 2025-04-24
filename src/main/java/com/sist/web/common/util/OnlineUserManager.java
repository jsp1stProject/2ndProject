package com.sist.web.common.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


public class OnlineUserManager {

	private static final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();
	
	public static void addSession(String userNo, String sessionId) {
		userSessions.compute(userNo, (key, sessions) -> {
			if (sessions == null) {
				sessions = new ConcurrentSkipListSet<>();
			}
			sessions.add(sessionId);
			return sessions;
		});
	}
	
	public static void removeSession(String sessionId) {
		for (Map.Entry<String, Set<String>> entry : userSessions.entrySet()) {
			Set<String> sessions = entry.getValue();
			if (sessions.remove(sessionId)) {
				if (sessions.isEmpty()) {
					userSessions.remove(entry.getKey());
				}
				break;
			}
		}
	}
	
	public static Set<String> getOnlineUsers() {
		return userSessions.keySet();
	}
	
	public static int getOnlineUserCount() {
		return userSessions.size();
	}
}
