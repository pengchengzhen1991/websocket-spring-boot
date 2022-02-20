package com.siwei.darwin.config;

import com.siwei.darwin.config.properties.InetUtilsProperties;
import com.siwei.darwin.config.properties.InstanceConfigBean;
import com.siwei.darwin.common.util.InetUtils;
import com.siwei.darwin.config.properties.SessionConfigBean;
import com.siwei.darwin.endpoint.WebsocketMessageClient;
import com.siwei.darwin.endpoint.HttpLogHandler;
import com.siwei.darwin.endpoint.WebsocketManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConditionalOnProperty(value = {"websocket.instance.enabled"}, matchIfMissing = true)
public class WebsocketInstanceAutoConfiguration {

    private final ConfigurableEnvironment env;

    @Bean
    public WebsocketMessageClient messageClient() {
        return new WebsocketMessageClient();
    }

    @Bean
    public HttpLogHandler sessionLogManager() {
        return new HttpLogHandler();
    }

    @Bean
    public WebsocketManager sessionManager() {
        return new WebsocketManager();
    }

    @Bean
    public SessionConfigBean sessionConfigBean() {
        return new SessionConfigBean();
    }

    @Bean
    public InstanceConfigBean instanceConfigBean(InetUtilsProperties properties) {
        return new InstanceConfigBean(new InetUtils(properties), env);
    }

    @Bean
    public InetUtilsProperties inetUtilsProperties() {
        return new InetUtilsProperties();
    }

}
