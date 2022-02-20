package com.siwei.darwin.config;

import com.siwei.darwin.endpoint.SimpleWebsocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import java.time.LocalDateTime;

@Slf4j
public class MyWebsocketHandler extends SimpleWebsocketHandler {

    @Override
    public void connect(WebSocketSession session) {
        log.info("建立ws连接: {}, principal: {}", session.getId(), session.getPrincipal());
    }

    @Override
    public void receiveTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("server 接受到消息: {}", payload);
        session.sendMessage(new TextMessage("server 发送给的消息" + payload + ",发送时间：{}" + LocalDateTime.now().toString()));
    }

    @Override
    public void receiveBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.info("发送二进制消息: {}", message);
    }

    @Override
    public void receivePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("发送pong消息: {}", message);
    }

    @Override
    public void error(WebSocketSession session, Throwable exception) throws Exception {
        log.info("异常处理");
    }

    @Override
    public void closed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("关闭");
    }

}
