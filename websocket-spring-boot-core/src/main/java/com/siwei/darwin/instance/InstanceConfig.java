package com.siwei.darwin.instance;

public interface InstanceConfig {

    String getInstanceId();

    String getAppname();

    String getIpAddress();

    String getHostname();

    Boolean getPreferIpAddress();

    Integer getNonSecurePort();

    Integer getSecurePort();

    Boolean getNonSecurePortEnabled();

    Boolean getSecurePortEnabled();

}
