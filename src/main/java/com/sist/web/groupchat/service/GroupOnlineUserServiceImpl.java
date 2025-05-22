package com.sist.web.groupchat.service;

import com.sist.web.group.dao.GroupDAO;
import com.sist.web.groupchat.dto.UserConnectionInfoDTO;
import com.sist.web.groupchat.dto.UserStatusDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupOnlineUserServiceImpl implements GroupOnlineUserService {

    private final SimpMessagingTemplate messagingTemplate;
    private final GroupDAO gDao;

    // 세션별 접속 유저 정보
    private final Map<String, List<UserConnectionInfoDTO>> sessionMap = new ConcurrentHashMap<>();

    // 유저 번호 → (그룹 번호 → 닉네임)
    private final Map<Long, Map<Long, String>> userGroupMap = new ConcurrentHashMap<>();

    // 전체 온라인 유저 번호
    private final Set<Long> globallyOnlineUsers = ConcurrentHashMap.newKeySet();

    @Override
    public void markOnline(String sessionId, long groupNo, long userNo, String nickname) {
        log.info("groupNo: {}, userNo: {}", groupNo, userNo);

        sessionMap.computeIfAbsent(sessionId, k -> new CopyOnWriteArrayList<>())
                  .add(new UserConnectionInfoDTO(userNo, groupNo, nickname));

        userGroupMap.computeIfAbsent(userNo, k -> new ConcurrentHashMap<>())
                    .put(groupNo, nickname);

        globallyOnlineUsers.add(userNo);
        gDao.updateLastSeenAt((int) userNo);

        getUserGroups(userNo).forEach(this::broadcast);
    }

    @Override
    public void markOffline(String sessionId) {
        List<UserConnectionInfoDTO> infos = sessionMap.remove(sessionId);
        if (infos == null) return;

        Set<Long> affectedUsers = infos.stream()
                                       .map(UserConnectionInfoDTO::getUserNo)
                                       .collect(Collectors.toSet());

        for (Long userNo : affectedUsers) {
            gDao.updateViewingZero(userNo.intValue());
            log.debug("연결 끊김 감지 viewing -> 0 완료: userNo={}", userNo);

            boolean stillConnected = sessionMap.values().stream()
                    .flatMap(List::stream)
                    .anyMatch(info -> info.getUserNo() == userNo);

            Map<Long, String> groupMap = userGroupMap.get(userNo);

            if (!stillConnected) {
                userGroupMap.remove(userNo);
                globallyOnlineUsers.remove(userNo);
            } else if (groupMap != null) {
                infos.stream()
                     .filter(info -> info.getUserNo() == userNo)
                     .map(UserConnectionInfoDTO::getGroupNo)
                     .forEach(groupMap::remove);

                if (groupMap.isEmpty()) {
                    userGroupMap.remove(userNo);
                    globallyOnlineUsers.remove(userNo);
                }
            }
        }

        Set<Long> affectedGroups = infos.stream()
                                        .map(UserConnectionInfoDTO::getGroupNo)
                                        .collect(Collectors.toSet());

        affectedGroups.forEach(this::broadcast);
    }

    @Override
    public List<UserStatusDTO> getOnlineUsersWithNickname(long groupNo) {
        return userGroupMap.entrySet().stream()
                .filter(entry -> entry.getValue().containsKey(groupNo))
                .map(entry -> new UserStatusDTO(entry.getKey(), entry.getValue().get(groupNo)))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Long> getGloballyOnlineUsers() {
        return globallyOnlineUsers;
    }

    private void broadcast(long groupNo) {
        List<UserStatusDTO> list = getOnlineUsersWithNickname(groupNo);
        log.info("브로드캐스트: group={}, users={}", groupNo, list);
        messagingTemplate.convertAndSend("/topic/groups/" + groupNo + "/online", list);
    }

    private Set<Long> getUserGroups(Long userNo) {
        return userGroupMap.getOrDefault(userNo, Map.of()).keySet();
    }
}
