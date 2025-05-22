package com.sist.web.groupchat.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sist.web.group.dao.GroupDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserTimeoutScheduler {
	private final GroupDAO gDao;
	
	@Scheduled(fixedDelay = 600000)
	public void handleInactiveUsers() {
		try {
			gDao.markInactiveUsers();
			log.debug("viewing=0 처리 완료");
		} catch (Exception ex) {
			log.error("viewing 상태 갱신 실패", ex);
		}
	}
}
