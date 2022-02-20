package com.siwei.darwin.http;

import com.siwei.darwin.endpoint.WebsocketMessageClient;
import com.siwei.darwin.model.SendMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Api(tags = "消息")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/websocket", produces = APPLICATION_JSON_VALUE)
public class MessageController {
    @Autowired
    private WebsocketMessageClient messageClient;

    @ApiOperation(value = "发送消息")
    @PostMapping("/message")
    public void pushMessage(@RequestBody SendMessage sendMessage) {
        log.info("sendMessage: {}", sendMessage);
        messageClient.sendMessage(sendMessage);
    }

}
