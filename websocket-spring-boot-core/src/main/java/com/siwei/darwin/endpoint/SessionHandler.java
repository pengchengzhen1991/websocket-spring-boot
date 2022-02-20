package com.siwei.darwin.endpoint;

import org.springframework.web.socket.*;

public interface SessionHandler {

    void connect(WebSocketSession session) throws Exception;

    void error(WebSocketSession session, Throwable exception) throws Exception;

    void closed(WebSocketSession session, CloseStatus status) throws Exception;

}
