package com.sist.web.groupchat.service;

import com.sist.web.common.exception.code.GroupErrorCode;
import com.sist.web.common.exception.domain.GroupException;
import com.sist.web.group.dao.GroupDAO;
import com.sist.web.groupchat.dao.GroupChatDAO;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.MessageSearchFilterDTO;
import com.sist.web.groupchat.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupChatServiceImpl implements GroupChatService {

    private final GroupChatDAO cDao;
    private final GroupDAO gDao;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${aws.url}")
    private String a3BaseUrl;

    @Transactional
    @Override
    public void saveAndSendGroupChatMessage(GroupChatDTO message) {
        message.setSender_nickname(cDao.selectGroupNickname(message.getGroup_no(), message.getSender_no()));

        cDao.insertGroupChatMessage(message);
        log.info("메세지 저장 성공: {}", message.getContent());

        GroupChatDTO saved = cDao.selectGroupChatByMessageNo(message.getMessage_no());

        sendStompMessage(saved);
        notifyUnreadUsers(saved);
    }

    @Override
    public List<GroupChatDTO> getLatestMessageByGroupNo(int groupNo, Long lastMessageNo) {
        try {
            List<GroupChatDTO> list = cDao.selectLatestMessageByGroupNo(groupNo, lastMessageNo);

            if (list != null && !list.isEmpty()) {
                applyProfileUrl(list);
                Collections.reverse(list);
            }

            return list != null ? list : Collections.emptyList();

        } catch (Exception ex) {
            log.error("이미지 불러오기 실패", ex);
            throw new GroupException(GroupErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    @Override
    public List<GroupChatDTO> getMessagesByFilters(MessageSearchFilterDTO dto) {
        return cDao.selectMessagesByFilters(dto);
    }

    @Override
    public List<GroupChatDTO> getMessagesAround(int groupNo, int messageNo) {
        Map<String, Object> param = new HashMap<>();
        param.put("groupNo", groupNo);
        param.put("messageNo", messageNo);
        return cDao.selectMessagesAround(param);
    }

    @Transactional
    @Override
    public void markViewing(int groupNo, int userNo, boolean isViewing) {
        int viewing = isViewing ? 1 : 0;
        gDao.updateViewingStatus(groupNo, userNo, viewing);
    }

    @Transactional
    @Override
    public void markExitAndUpdateLastRead(int groupNo, int userNo) {
        Long latestMessageNo = cDao.selectLatestMessageNo(groupNo);
        if (latestMessageNo != null) {
            gDao.updateExitStatus(groupNo, userNo, latestMessageNo);
        }
    }

    private void sendStompMessage(GroupChatDTO message) {
        try {
            messagingTemplate.convertAndSend("/sub/chats/groups/" + message.getGroup_no(), message);
            log.info("STOMP 메세지 전송 성공 - groupNo: {}, content: {}", message.getGroup_no(), message.getContent());
        } catch (Exception ex) {
            log.error("STOMP 메세지 전송 실패 - groupNo: {}, error: {}", message.getGroup_no(), ex.getMessage());
            throw new RuntimeException("STOMP 전송 실패", ex);
        }
    }

    private void notifyUnreadUsers(GroupChatDTO message) {
        List<Long> notifyUserNos = cDao.selectUsersToNotify(message.getGroup_no(), message.getMessage_no(), message.getSender_no());
        for (Long userNo : notifyUserNos) {
            messagingTemplate.convertAndSendToUser(
                String.valueOf(userNo),
                "/queue/notify",
                new NotificationDTO("새 메시지 도착", message.getGroup_no(), message.getSender_nickname())
            );
            log.info("알림 전송 userNo: {}, groupNo: {}", userNo, message.getGroup_no());
        }
    }

    private void applyProfileUrl(List<GroupChatDTO> list) {
        for (GroupChatDTO dto : list) {
            if (dto.getProfile() != null && !dto.getProfile().isBlank()) {
                dto.setProfile(a3BaseUrl + dto.getProfile());
            }
        }
    }
}
