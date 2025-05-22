package com.sist.web.groupchat.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.GroupErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.GroupException;
import com.sist.web.common.response.ApiResponse;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.MessageSearchFilterDTO;
import com.sist.web.groupchat.service.GroupChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/chats/groups")
@RequiredArgsConstructor
public class GroupChatRestController {

    private final GroupChatService chatService;

    @GetMapping("/{groupNo}/messages")
    public ResponseEntity<ApiResponse<List<GroupChatDTO>>> getLatestMessages(
            @PathVariable @Min(value = 1, message = "유효하지 않은 그룹 번호입니다") Integer groupNo,
            @RequestParam(required = false) Long lastMessageNo) {

        List<GroupChatDTO> messages = chatService.getLatestMessageByGroupNo(groupNo, lastMessageNo);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    @GetMapping("/{groupNo}/messages/search")
    public ResponseEntity<ApiResponse<List<GroupChatDTO>>> getMessagesByFilters(
            @PathVariable @Min(value = 1, message = "유효하지 않은 그룹 번호입니다") Integer groupNo,
            @ModelAttribute MessageSearchFilterDTO dto) {

        if (!dto.isValid()) {
            log.error("메세지 검색 유효성 검사 실패 - startDate={}, endDate={}", dto.getStartDate(), dto.getEndDate());
            throw new CommonException(CommonErrorCode.INVALID_PARAMETER);
        }

        dto.setGroupNo(groupNo);
        List<GroupChatDTO> list = chatService.getMessagesByFilters(dto);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/{groupNo}/messages/around")
    public ResponseEntity<ApiResponse<List<GroupChatDTO>>> getMessagesAround(
            @PathVariable @Min(value = 1, message = "유효하지 않은 그룹 번호입니다") Integer groupNo,
            @RequestParam @Min(value = 1, message = "유효하지 않은 메세지 번호입니다") Integer messageNo) {

        List<GroupChatDTO> list = chatService.getMessagesAround(groupNo, messageNo);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PatchMapping("/{groupNo}/viewing")
    public ResponseEntity<ApiResponse<Void>> updateViewingStatus(
            @PathVariable int groupNo,
            @RequestParam("viewing") boolean viewing,
            HttpServletRequest request) {

        int userNo = resolveUserNo(request);
        chatService.markViewing(groupNo, userNo, viewing);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{groupNo}/exit")
    public ResponseEntity<ApiResponse<Void>> exitGroupChat(
            @PathVariable int groupNo,
            HttpServletRequest request) {

        int userNo = resolveUserNo(request);
        chatService.markExitAndUpdateLastRead(groupNo, userNo);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private int resolveUserNo(HttpServletRequest request) {
        Object userAttr = request.getAttribute("userno");
        if (userAttr instanceof Long) {
            return ((Long) userAttr).intValue();
        }
        throw new GroupException(GroupErrorCode.USER_NOT_FOUND);
    }
}
