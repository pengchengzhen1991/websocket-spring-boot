package com.siwei.darwin.endpoint;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public interface LogHandler {

    void connect(WebSocketSession session) throws Exception;

    void record(WebSocketSession session, WebSocketMessage<?> message) throws Exception;

    void error(WebSocketSession session, Throwable exception) throws Exception;

    void close(WebSocketSession session, CloseStatus status) throws Exception;

}
