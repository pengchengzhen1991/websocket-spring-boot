package com.siwei.darwin.endpoint;

import com.siwei.darwin.common.constant.CommonConstants;
import com.siwei.darwin.common.exception.WebsocketException;
import com.siwei.darwin.config.properties.InstanceConfigBean;
import com.siwei.darwin.model.MessageSendResult;
import com.siwei.darwin.model.SendMessage;
import com.siwei.darwin.instance.InstanceClient;
import com.siwei.darwin.instance.RegistryInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebsocketManager implements SessionHandler, MessageHandler {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSession>> URI_SESSION_POOL = new ConcurrentHashMap<>();
    @Autowired
    private SessionRemoteHandler sessionRemoteHandler;
    @Autowired
    private MessageRemoteHandler messageRemoteHandler;
    @Autowired
    private LogHandler logHandler;
    @Autowired
    private InstanceConfigBean instanceConfigBean;
    @Autowired
    private InstanceClient instanceClient;

    @Override
    public void connect(WebSocketSession session) throws Exception {
        this.URI_SESSION_POOL.computeIfAbsent(this.getUri(session), s -> new ConcurrentHashMap<>()).put(this.getUserId(session), session);
        this.sessionRemoteHandler.registry(session);
        this.logHandler.connect(session);
    }

    private String getUri(WebSocketSession session) {
        String uri = session.getUri().getPath();
        return uri.startsWith(CommonConstants.VIRGULE) ? uri.substring(1) : uri;
    }

    private String getUserId(WebSocketSession session) {
        Principal principal = session.getPrincipal();
        if (Objects.isNull(principal)) {
            throw new WebsocketException("no user principal");
        }
        return principal.getName();
    }

    @Override
    public void receiveMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        this.logHandler.record(session, message);
    }

    @Override
    public void error(WebSocketSession session, Throwable exception) throws Exception {
        this.logHandler.error(session, exception);
    }

    @Override
    public void closed(WebSocketSession session, CloseStatus status) throws Exception {
        this.sessionRemoteHandler.cancel(session);
        this.logHandler.close(session, status);
        this.removeSession(session);
    }

    private void removeSession(WebSocketSession session) {
        this.URI_SESSION_POOL.computeIfAbsent(this.getUri(session), s -> new ConcurrentHashMap<>()).remove(this.getUserId(session));
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventListener(WebsocketMessageEvent.class)
    public void listenerMessage(WebsocketMessageEvent event) {
        SendMessage sendMessage = event.getSendMessage();
        if (StringUtils.hasText(sendMessage.getTargetInstanceId())) {
            if (!Objects.equals(this.instanceConfigBean.getInstanceId(), sendMessage.getTargetInstanceId())) {
                return;
            }
            this.send(sendMessage);
        } else {
            List<String> instances = sessionRemoteHandler.getInstances(sendMessage.getUri(), sendMessage.getUserId());
            for (String instanceId : instances) {
                sendMessage.setFromInstanceId(this.instanceConfigBean.getInstanceId()).setTargetInstanceId(instanceId);
                this.send(sendMessage);
            }
        }
    }

    private void send(SendMessage sendMessage) {
        if (Objects.equals(this.instanceConfigBean.getInstanceId(), sendMessage.getTargetInstanceId())) {
            if (Objects.nonNull(sendMessage.getUri())) {
                this.sendMessage(this.URI_SESSION_POOL.computeIfAbsent(sendMessage.getUri(), s -> new ConcurrentHashMap<>()), sendMessage);
            } else {
                this.URI_SESSION_POOL.values().forEach(sessionMap -> this.sendMessage(sessionMap, sendMessage));
            }
        } else {
            RegistryInstance registryInstance = instanceClient.discover(sendMessage.getTargetInstanceId());
            if (null != registryInstance) {
                MessageSendResult result = messageRemoteHandler.sendMessage(registryInstance, sendMessage);
                this.recordResult(result);
            }
        }
    }

    private void sendMessage(ConcurrentHashMap<String, WebSocketSession> sessionMap, SendMessage sendMessage) {
        this.sendMessage(sessionMap.get(sendMessage.getUserId()), sendMessage);
    }

    private void sendMessage(WebSocketSession session, SendMessage sendMessage) {
        if (session != null) {
            try {
                session.sendMessage(new TextMessage(sendMessage.getText()));
            } catch (Exception e) {
                // TODO 记录消息发送失败
            }
        }
    }

    private void recordResult(MessageSendResult result) {

    }

}
