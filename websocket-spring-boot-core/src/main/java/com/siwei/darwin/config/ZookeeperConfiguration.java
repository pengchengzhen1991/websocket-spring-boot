package com.siwei.darwin.config;

import com.siwei.darwin.config.properties.InstanceConfigBean;
import com.siwei.darwin.config.properties.SessionConfigBean;
import com.siwei.darwin.config.properties.ZookeeperServerProperties;
import com.siwei.darwin.endpoint.SessionRemoteHandler;
import com.siwei.darwin.endpoint.ZookeeperSessionRemoteHandler;
import com.siwei.darwin.instance.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ZookeeperConfiguration {

    @Bean
    @ConditionalOnProperty(value = "websocket.session.registry-type", havingValue = "ZOOKEEPER", matchIfMissing = true)
    public SessionRemoteHandler sessionRemoteManager(ZookeeperCuratorClient curatorClient,
                                                     SessionConfigBean sessionConfigBean,
                                                     InstanceConfigBean instanceConfigBean) {
        return new ZookeeperSessionRemoteHandler(curatorClient, sessionConfigBean, instanceConfigBean);
    }

    @Bean
    @ConditionalOnProperty(value = "websocket.instance.registry-type", havingValue = "ZOOKEEPER", matchIfMissing = true)
    public InstanceClient instanceClient(ZookeeperCuratorClient curatorClient, InstanceConfigBean instanceConfigBean) {
        ZookeeperClient zookeeperClient = new ZookeeperClient(curatorClient, instanceConfigBean);
        return new ZookeeperInstanceClient(zookeeperClient);
    }

    @Bean
    public ZookeeperCuratorClient zookeeperCuratorClient(ZookeeperServerProperties serverProperties) {
        return ZookeeperCuratorClientFactory.create(serverProperties);
    }

    @Bean
    public ZookeeperServerProperties zookeeperServerProperties() {
        return new ZookeeperServerProperties();
    }

}
