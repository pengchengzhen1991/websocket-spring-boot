package com.siwei.darwin.config.properties;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.cloud.inetutils")
public class InetUtilsProperties {

    private String defaultHostname = "localhost";

    private String defaultIpAddress = "127.0.0.1";

    @Value("${spring.util.timeout.sec:${SPRING_UTIL_TIMEOUT_SEC:1}}")
    private int timeoutSeconds = 1;

    private List<String> ignoredInterfaces = new ArrayList<>();

    private boolean useOnlySiteLocalInterfaces = false;

    private List<String> preferredNetworks = new ArrayList<>();

}

