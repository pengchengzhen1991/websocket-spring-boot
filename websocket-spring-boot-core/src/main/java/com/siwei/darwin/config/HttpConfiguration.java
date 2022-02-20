package com.siwei.darwin.config;

import com.siwei.darwin.endpoint.HttpMessageRemoteHandler;
import com.siwei.darwin.endpoint.MessageRemoteHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class HttpConfiguration {

    @Bean
    @ConditionalOnProperty(value = "websocket.instance.msg-mode", havingValue = "HTTP", matchIfMissing = true)
    public MessageRemoteHandler messageRemoteHandler() {
        return new HttpMessageRemoteHandler();
    }

}
