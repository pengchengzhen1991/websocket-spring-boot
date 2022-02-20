package com.siwei.darwin.endpoint;

import com.siwei.darwin.model.SendMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WebsocketMessageEvent extends ApplicationEvent {

    private final SendMessage sendMessage;

    WebsocketMessageEvent(SendMessage sendMessage) {
        super(sendMessage);
        this.sendMessage = sendMessage;
    }

}
