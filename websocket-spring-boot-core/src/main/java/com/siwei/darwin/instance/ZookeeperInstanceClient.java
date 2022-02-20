package com.siwei.darwin.instance;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ZookeeperInstanceClient implements InstanceClient {
    private final ZookeeperClient zookeeperClient;

    @Override
    public boolean registry() {
        return zookeeperClient.registryInstance();
    }

    @Override
    public boolean registry(RegistryInstance instance) {
        return zookeeperClient.registryInstance(instance);
    }

    @Override
    public boolean flush() {
        return false;
    }

    @Override
    public RegistryInstance discover(String instanceId) {
        return zookeeperClient.discoverInstance(instanceId);
    }

    @Override
    public List<RegistryInstance> discover() {
        return zookeeperClient.discoverInstances();
    }

}
