package com.sist.web.sitterchat.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class SitterChatExceptionHandler {

    @MessageExceptionHandler(SitterChatException.class)
    @SendToUser("/queue/errors")
    public String handleChatException(SitterChatException ex) {
        return ex.getMessage();
    }

    @MessageExceptionHandler(SecurityException.class)
    @SendToUser("/queue/errors")
    public String handleSecurityException(SecurityException ex) {
        return "인증 오류: " + ex.getMessage();
    }

    @MessageExceptionHandler(IllegalArgumentException.class)
    @SendToUser("/queue/errors")
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return "입력 오류: " + ex.getMessage();
    }
}
