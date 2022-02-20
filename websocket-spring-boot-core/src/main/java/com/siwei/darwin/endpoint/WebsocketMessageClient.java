package com.siwei.darwin.endpoint;

import com.siwei.darwin.model.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public class WebsocketMessageClient {
    @Autowired
    private ApplicationEventPublisher publisher;

    public void sendMessage(SendMessage sendMessage) {
        publisher.publishEvent(new WebsocketMessageEvent(sendMessage));
    }

}
