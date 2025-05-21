package com.sist.web.groupchat.websocket.interceptor;

import com.sist.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwt;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        URI uri = request.getURI();
        String query = uri.getQuery();
        String token = extractTokenFromQuery(query);

        if (token != null && jwt.validateToken(token)) {
            String userNo = jwt.getUserNoFromToken(token);
            Principal principal = new StompPrincipal(userNo);
            attributes.put("principal", principal); // ✅ 세션에 주입
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }

    private String extractTokenFromQuery(String query) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            if (param.startsWith("Authorization=Bearer%20")) {
                return param.substring("Authorization=Bearer%20".length());
            }
        }
        return null;
    }
}
