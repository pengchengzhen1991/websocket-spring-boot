package com.siwei.darwin.instance;

public interface InstanceRegister {

    boolean registry();

    boolean registry(RegistryInstance instance);

    boolean flush();

}
