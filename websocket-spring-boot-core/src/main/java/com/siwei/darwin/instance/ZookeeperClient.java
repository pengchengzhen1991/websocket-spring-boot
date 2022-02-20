package com.siwei.darwin.instance;

import com.alibaba.fastjson.JSON;
import com.siwei.darwin.config.properties.InstanceConfigBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Data
public class ZookeeperClient implements Closeable {
    private final ZookeeperCuratorClient curatorClient;
    private final InstanceConfigBean instanceConfigBean;
    private final ConcurrentHashMap<String, RegistryInstance> instanceConcurrentHashMap;

    public ZookeeperClient(ZookeeperCuratorClient curatorClient, InstanceConfigBean instanceConfigBean) {
        this.curatorClient = curatorClient;
        this.instanceConfigBean = instanceConfigBean;
        this.instanceConcurrentHashMap = new ConcurrentHashMap<>();
        this.connectAndWatch();
    }

    private void connectAndWatch() {
        boolean result = this.curatorClient.connect();
        if (result) {
            this.curatorClient.pathChildrenCache(this.instanceConfigBean.getUrlPrefix(), (curatorFramework, event) -> {
                log.info(" pathChildrenCacheEvent: {}", event);
                switch (event.getType()) {
                    case CHILD_ADDED:
                    case CHILD_UPDATED:
                        if (null != event.getData()) {
                            RegistryInstance instance = JSON.parseObject(new String(event.getData().getData()), RegistryInstance.class);
                            this.instanceConcurrentHashMap.put(instance.getInstanceId(), instance);
                        }
                        break;
                    case CHILD_REMOVED:
                        if (null != event.getData()) {
                            RegistryInstance instance = JSON.parseObject(new String(event.getData().getData()), RegistryInstance.class);
                            this.instanceConcurrentHashMap.remove(instance.getInstanceId());
                        }
                        break;
                    default:
                        break;
                }
            });
            this.registryInstance();
        }

    }

    public boolean registryInstance() {
        return this.registryInstance(this.instanceConfigBean.getRegistryInstance());
    }

    public boolean registryInstance(RegistryInstance instance) {
        try {
            String path = this.instanceConfigBean.getInstancePath(instance.getInstanceId());
            if (!this.curatorClient.exist(path)) {
                this.curatorClient.create(path, JSON.toJSONString(instance), CreateMode.EPHEMERAL, true);
                this.instanceConcurrentHashMap.put(instance.getInstanceId(), instance);
            }
            return true;
        } catch (Exception e) {
            log.info("websocket instance registry", e);
        }
        return false;
    }

    public RegistryInstance discoverInstance(String instanceId) {
        if (this.instanceConcurrentHashMap.containsKey(instanceId)) {
            return instanceConcurrentHashMap.get(instanceId);
        }
        String path = this.instanceConfigBean.getInstancePath(instanceId);
        RegistryInstance instance = (RegistryInstance) this.curatorClient.readObject(path, false, RegistryInstance.class);
        return this.flushRegistryInstance(instance);
    }

    public List<RegistryInstance> discoverInstances() {
        List<String> dataList = this.curatorClient.getChildrenData(instanceConfigBean.getUrlPrefix(), false);
        List<RegistryInstance> registryInstances = dataList.stream()
                .map(str -> JSON.parseObject(str, RegistryInstance.class))
                .collect(Collectors.toList());
        this.flushRegistryInstances(registryInstances);
        return registryInstances;
    }

    private void flushRegistryInstances(List<RegistryInstance> registryInstances) {
        if (registryInstances != null) {
            for (RegistryInstance instance : registryInstances) {
                this.flushRegistryInstance(instance);
            }
        }
    }

    private RegistryInstance flushRegistryInstance(RegistryInstance registryInstance) {
        if (registryInstance == null) {
            return null;
        }
        return this.instanceConcurrentHashMap.put(registryInstance.getInstanceId(), registryInstance);
    }

    @Override
    public void close() throws IOException {
        try {
            RegistryInstance instance = this.instanceConfigBean.getRegistryInstance();
            String path = this.instanceConfigBean.getInstancePath(instance.getInstanceId());
            if (this.curatorClient.exist(path)) {
                this.curatorClient.delete(path);
                this.instanceConcurrentHashMap.put(instance.getInstanceId(), instance);
            }
        } catch (Exception e) {
            log.info("websocket instance registry", e);
        }
    }
}
