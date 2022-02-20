package com.siwei.darwin.instance;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegistryInstance implements InstanceConfig {
    private String instanceId;
    private String appname = "WebsocketApplication";
    private String ipAddress;
    private String hostname;
    private Boolean preferIpAddress = false;
    private Integer nonSecurePort = 80;
    private Integer securePort = 443;
    private Boolean nonSecurePortEnabled = true;
    private Boolean securePortEnabled;

    public String getHostname() {
        return this.getHostname(false);
    }

    public String getHostname(boolean refresh) {
        return this.preferIpAddress ? this.ipAddress : this.hostname;
    }

}
