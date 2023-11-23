package com.study.sociallogin.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.thymeleaf.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class EchoHandler extends TextWebSocketHandler{

    // 전체 로그인 유저
    private List<WebSocketSession> sessions = new ArrayList<>();

    // 1대1 매핑
    private Map<String, WebSocketSession> userSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Socket 연결");
        String user = (String) session.getAttributes().get("userId");
        System.out.println("userId = " + user);
        sessions.add(session);
        log.info(sendPushUsername(session));				//현재 접속한 사람의 username이 출력됨
        String senderId = sendPushUsername(session);
        userSessionMap.put(user, session);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Socket 연결 해제");
        sessions.remove(session);
        userSessionMap.remove(sendPushUsername(session), session);
    }

    //알람을 보내는 유저(댓글작성, 좋아요 누르는 유저)
    private String sendPushUsername(WebSocketSession session) {
        String loginUsername;

        if (session.getPrincipal() == null) {
            loginUsername = null;
        } else {
            loginUsername = session.getPrincipal().getName();
        }
        return loginUsername;
    }

    public WebSocketSession getSession(String userId) {
        return userSessionMap.get(userId);
    }
    public void sendMessageToSession(WebSocketSession session, String message) throws Exception {
        if (session != null && session.isOpen()) {
            System.out.println("sendMessageToSession() message = " + message);
            session.sendMessage(new TextMessage(message));
        }
    }
}
