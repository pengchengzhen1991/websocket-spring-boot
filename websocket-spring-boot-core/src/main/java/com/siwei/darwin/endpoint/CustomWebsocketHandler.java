package com.siwei.darwin.endpoint;

import org.springframework.web.socket.*;

public interface CustomWebsocketHandler extends SessionHandler, MessageHandler {

    void receiveTextMessage(WebSocketSession session, TextMessage message) throws Exception;

    void receiveBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception;

    void receivePongMessage(WebSocketSession session, PongMessage message) throws Exception;

}
