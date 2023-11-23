package com.study.sociallogin.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class MyHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // URL 쿼리 파라미터에서 사용자 식별자 추출
        String userId = request.getURI().getQuery().split("=")[1];
        attributes.put("userId", userId);
        System.out.println("MyHandshakeInterceptor.beforeHandshake() userId = " + userId);

        // 쿠키에서 세션 정보 추출 (옵션)
        // Cookie[] cookies = request.getCookies();
        // ...

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}