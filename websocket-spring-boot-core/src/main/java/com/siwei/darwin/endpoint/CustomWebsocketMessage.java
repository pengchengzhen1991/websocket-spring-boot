package com.siwei.darwin.endpoint;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.socket.TextMessage;

@Data
@Accessors(chain = true)
public class CustomWebsocketMessage{

    private Long id;

    private String uri;

    private String userId;

    private TextMessage message;

    private String fromInstanceId;

    private String targetInstanceId;

}
