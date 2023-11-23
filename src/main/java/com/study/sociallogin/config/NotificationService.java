package com.study.sociallogin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class NotificationService {

    private final EchoHandler handler;

    @Autowired
    public NotificationService(EchoHandler handler) {
        this.handler = handler;
    }

    public void notifyUser(String userId, String message) throws Exception {
        WebSocketSession session = handler.getSession(userId);
        if (session != null) {
            System.out.println("session is exist");
            handler.sendMessageToSession(session, message);
        }
        System.out.println("session is null");
    }
}

