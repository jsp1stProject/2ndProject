package com.sist.web.groupchat.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sist.web.groupchat.dto.UserConnectionInfoDTO;
import com.sist.web.groupchat.dto.UserStatusDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupOnlineUserServiceImpl implements GroupOnlineUserService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, List<UserConnectionInfoDTO>> sessionMap = new ConcurrentHashMap<>();
    private final Map<Long, Map<Long, String>> groupNicknames = new ConcurrentHashMap<>();

    @Override
    public void markOnline(String sessionId, long groupNo, long userNo, String nickname) {
    	log.info("groupNo: {}, userNo:{}", groupNo, userNo);
        sessionMap.computeIfAbsent(sessionId, k -> Collections.synchronizedList(new ArrayList<>()))
                  .add(new UserConnectionInfoDTO(userNo, groupNo, nickname));

        groupNicknames.computeIfAbsent(groupNo, k -> new ConcurrentHashMap<>())
                      .put(userNo, nickname);

        broadcast(groupNo);
    }

    @Override
    public void markOffline(String sessionId) {
        List<UserConnectionInfoDTO> infos = sessionMap.remove(sessionId);
        if (infos == null) return;

        Set<Long> groups = infos.stream()
                                 .map(UserConnectionInfoDTO::getGroupNo)
                                 .collect(Collectors.toSet());
        for (Long groupNo : groups) {
            Map<Long, String> nickMap = groupNicknames.get(groupNo);
            if (nickMap != null) {
                infos.stream()
                     .filter(info -> info.getGroupNo() == groupNo)
                     .map(UserConnectionInfoDTO::getUserNo)
                     .forEach(nickMap::remove);
                if (nickMap.isEmpty()) {
                    groupNicknames.remove(groupNo);
                }
            }
            broadcast(groupNo);
        }
    }

    @Override
    public List<UserStatusDTO> getOnlineUsersWithNickname(long groupNo) {
    	Map<Long, String> nickMap = groupNicknames.getOrDefault(groupNo, Map.of());
        return nickMap.entrySet().stream()
                     .map(e -> new UserStatusDTO(e.getKey(), e.getValue()))
                     .collect(Collectors.toList());
    }

    private void broadcast(long groupNo) {
        List<UserStatusDTO> list = getOnlineUsersWithNickname(groupNo);
        messagingTemplate.convertAndSend("/topic/groups/" + groupNo + "/online", list);
    }
	

}
