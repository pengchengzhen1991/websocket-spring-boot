package com.siwei.darwin.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

@Slf4j
public abstract class SimpleWebsocketHandler implements WebSocketHandler, CustomWebsocketHandler {
    @Autowired
    private WebsocketManager sessionManager;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.sessionManager.connect(session);
        this.connect(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        this.sessionManager.receiveMessage(session, message);
        this.receiveMessage(session, message);
    }

    @Override
    public void receiveMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            this.receiveTextMessage(session, (TextMessage) message);
        } else if (message instanceof BinaryMessage) {
            this.receiveBinaryMessage(session, (BinaryMessage) message);
        } else {
            if (!(message instanceof PongMessage)) {
                throw new IllegalStateException("Unexpected WebSocket message type: " + message);
            }
            this.receivePongMessage(session, (PongMessage) message);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        this.sessionManager.error(session, exception);
        this.error(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        this.sessionManager.closed(session, status);
        this.closed(session, status);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
