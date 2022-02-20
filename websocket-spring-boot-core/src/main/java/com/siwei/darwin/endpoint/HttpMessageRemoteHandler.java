package com.siwei.darwin.endpoint;

import com.siwei.darwin.model.MessageSendResult;
import com.siwei.darwin.model.SendMessage;
import com.siwei.darwin.instance.RegistryInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpMessageRemoteHandler implements MessageRemoteHandler {
    private static final String HTTP = "http://";
    private static final String MESSAGE_URI = "/api/v1/websocket/message";
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public MessageSendResult sendMessage(RegistryInstance instance, SendMessage sendMessage) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(this.getSendMessageUrl(instance), sendMessage, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {

        }
        // TODO 返回发送消息的结果
        return new MessageSendResult();
    }

    private String getSendMessageUrl(RegistryInstance instance) {
        return HTTP + instance.getHostname() + ":" + instance.getNonSecurePort() + MESSAGE_URI;
    }

}
