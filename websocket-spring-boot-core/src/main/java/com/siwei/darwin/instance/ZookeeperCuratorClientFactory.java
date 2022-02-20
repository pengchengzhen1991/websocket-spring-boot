package com.siwei.darwin.instance;

import com.siwei.darwin.config.properties.ZookeeperServerProperties;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperCuratorClientFactory {

    public static ZookeeperCuratorClient create(ZookeeperServerProperties serverProperties) {
        RetryPolicy retry = new ExponentialBackoffRetry(serverProperties.getRetryBaseSleepTime(), serverProperties.getMaxRetries());
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(serverProperties.getConnectUrl())
                .sessionTimeoutMs(serverProperties.getSessionTimeout())
                .retryPolicy(retry)
                .namespace(serverProperties.getNamespace())
                .build();
        return new ZookeeperCuratorClient(curatorFramework);
    }

}
