package com.project.RunGame.game.controller.websockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final GameWebSocket gameWebSocket;
	
    @Autowired
    public WebSocketConfig(GameWebSocket gameWebSocket) {
        this.gameWebSocket = gameWebSocket;
    }
	@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.gameWebSocket, "/gameWs")
                .setAllowedOrigins("*");
    }
}