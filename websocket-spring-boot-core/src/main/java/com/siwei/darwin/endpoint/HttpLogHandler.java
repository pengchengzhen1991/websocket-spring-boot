package com.siwei.darwin.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class HttpLogHandler implements LogHandler {

    @Override
    public void connect(WebSocketSession session) throws Exception {

    }

    @Override
    public void record(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void error(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void close(WebSocketSession session, CloseStatus status) throws Exception {

    }

}
