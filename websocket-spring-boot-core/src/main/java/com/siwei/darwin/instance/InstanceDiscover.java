package com.siwei.darwin.instance;

import java.util.List;

public interface InstanceDiscover {

    RegistryInstance discover(String instanceId);

    List<RegistryInstance> discover();

}
