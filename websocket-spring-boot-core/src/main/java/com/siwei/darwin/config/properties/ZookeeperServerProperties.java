package com.siwei.darwin.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("websocket.zookeeper-server")
public class ZookeeperServerProperties {

    private String connectUrl = "127.0.0.1:2181";

    private Integer sessionTimeout = 3000;

    private Integer retryBaseSleepTime = 1000;

    private Integer maxRetries = 3;

    private String namespace = "";

}
