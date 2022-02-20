package com.siwei.darwin.config.properties;

import com.siwei.darwin.common.enums.MessageModeEnum;
import com.siwei.darwin.common.enums.InstanceRegistryTypeEnum;
import com.siwei.darwin.common.util.InetUtils;
import com.siwei.darwin.instance.InstanceConfig;
import com.siwei.darwin.instance.RegistryInstance;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Data
@ConfigurationProperties("websocket.instance")
public class InstanceConfigBean implements InstanceConfig {
    private InetUtils inetUtils;
    private InetUtils.HostInfo hostInfo;
    private Environment environment;
    private InstanceRegistryTypeEnum registryType = InstanceRegistryTypeEnum.ZOOKEEPER;
    private MessageModeEnum msgMode = MessageModeEnum.HTTP;
    private String urlPrefix = "/websocket/instance";
    private String instanceId;
    private String appname = "WebsocketApplication";
    private String ipAddress;
    private String hostname;
    private Boolean preferIpAddress;
    private Integer nonSecurePort;
    private Integer securePort = 443;
    private Boolean nonSecurePortEnabled = true;
    private Boolean securePortEnabled;


    public InstanceConfigBean(InetUtils inetUtils, Environment environment) {
        this.environment = environment;
        String springAppName = this.getProperty("spring.application.name", "");
        if (StringUtils.hasText(springAppName)) {
            this.appname = springAppName;
        }

        this.inetUtils = inetUtils;
        this.hostInfo = this.inetUtils.findFirstNonLoopbackHostInfo();
        if (!StringUtils.hasText(this.ipAddress)) {
            this.ipAddress = this.hostInfo.getIpAddress();
        }
        if (!StringUtils.hasText(this.hostname)) {
            this.hostname = this.hostInfo.getHostname();
        }
        if (Objects.isNull(this.preferIpAddress)) {
            this.preferIpAddress = false;
        }

        String defaultSecurePortEnabled = this.getProperty("eureka.instance.secure-port-enabled", "false");
        boolean isSecurePortEnabled = Boolean.parseBoolean(this.getProperty("websocket.instance.secure-port-enabled", defaultSecurePortEnabled));
        int serverPort = Integer.parseInt(this.getProperty("server.port", this.getProperty("port", "8080")));
        this.securePortEnabled = isSecurePortEnabled;
        this.nonSecurePort = serverPort;
        if (isSecurePortEnabled) {
            this.securePort = serverPort;
        }
        this.instanceId = this.ipAddress + ":" + this.nonSecurePort;

        log.info("WebsocketInstanceConfigBean: {}", this);
    }

    private String getProperty(String property) {
        return getProperty(property, "");
    }

    private String getProperty(String property, String defaultValue) {
        return this.environment.containsProperty(property) ? this.environment.getProperty(property) : defaultValue;
    }

    public String getInstancePath() {
        return this.getInstancePath(this.instanceId);
    }

    public String getInstancePath(String instanceId) {
        return this.urlPrefix + "/" + instanceId;
    }

    public String getCurrentInstanceId() {
        return this.instanceId;
    }

    public String getHostname() {
        return this.getHostname(false);
    }

    public String getHostname(boolean refresh) {
        return this.preferIpAddress ? this.ipAddress : this.hostname;
    }

    public RegistryInstance getRegistryInstance() {
        return new RegistryInstance()
                .setInstanceId(this.instanceId)
                .setAppname(this.appname)
                .setIpAddress(this.ipAddress)
                .setHostname(this.hostname)
                .setPreferIpAddress(this.preferIpAddress)
                .setNonSecurePort(this.nonSecurePort)
                .setSecurePort(this.securePort)
                .setNonSecurePortEnabled(this.nonSecurePortEnabled)
                .setSecurePortEnabled(this.securePortEnabled);
    }

}
