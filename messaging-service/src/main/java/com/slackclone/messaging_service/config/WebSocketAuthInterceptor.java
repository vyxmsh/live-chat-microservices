package com.slackclone.messaging_service.config;

import com.slackclone.messaging_service.util.JwtUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    
    private final JwtUtil jwtUtil;

    public WebSocketAuthInterceptor(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel)
    {
        StompHeaderAccessor accessor = 
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand()))
        {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer")){
                throw new IllegalArgumentException("Missing or Invalid Authorization header");
            }

            String token = authHeader.substring(7);
            if(!jwtUtil.validateToken(token)){
                throw new IllegalArgumentException("Invalid JWT token");
            }
            
            String username = jwtUtil.extractUsername(token);
            Long userId = jwtUtil.extractUserId(token);
            accessor.getSessionAttributes().put("Username", username);
            accessor.getSessionAttributes().put("UserId", userId);
        }

        return message;
    }
}
