package com.siwei.darwin.config.properties;

import com.siwei.darwin.common.enums.SessionRegistryTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Data
@ConfigurationProperties("websocket.session")
public class SessionConfigBean {

    private SessionRegistryTypeEnum registryType = SessionRegistryTypeEnum.ZOOKEEPER;

    private String urlPrefix = "/websocket/session";

}
