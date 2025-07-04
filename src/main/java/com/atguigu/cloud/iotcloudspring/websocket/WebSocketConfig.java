package com.atguigu.cloud.iotcloudspring.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//这个是基于STOMP 协议的嗷~
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 客户端订阅 /topic/**
        registry.enableSimpleBroker("/topic");
        // 客户端发消息前缀 /app/**
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 纯 WebSocket 端点，微信小程序等原生 WS 客户端用
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*");

        // SockJS 回退端点，浏览器 + SockJS 客户端用
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}

