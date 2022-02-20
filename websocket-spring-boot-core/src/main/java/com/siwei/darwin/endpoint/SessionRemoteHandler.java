package com.siwei.darwin.endpoint;

import com.siwei.darwin.model.UserSessionInfo;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface SessionRemoteHandler {

    void registry(WebSocketSession session);

    List<String> getInstances(String uri, String userName);

    List<UserSessionInfo> getUserInstanceInfoes();

    void cancel(WebSocketSession session);

}
