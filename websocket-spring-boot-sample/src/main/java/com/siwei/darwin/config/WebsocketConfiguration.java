package com.siwei.darwin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {
    @Autowired
    private MyWebsocketHandler myWsHandler;
    @Autowired
    private PrincipalHandshakeHandler principalHandshakeHandler;


    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWsHandler, "myWs")
                .setAllowedOrigins("*")
                .setHandshakeHandler(principalHandshakeHandler);
    }

    @Bean
    public PrincipalHandshakeHandler principalHandshakeHandler() {
        return new PrincipalHandshakeHandler();
    }

    @Bean
    public MyWebsocketHandler myWebSocketHandler() {
        return new MyWebsocketHandler();
    }

}
