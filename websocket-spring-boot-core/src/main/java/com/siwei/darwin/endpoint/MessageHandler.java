package com.siwei.darwin.endpoint;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public interface MessageHandler {

    void receiveMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception;

}
