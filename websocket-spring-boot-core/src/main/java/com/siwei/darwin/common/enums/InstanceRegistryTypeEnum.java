package com.siwei.darwin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstanceRegistryTypeEnum implements ICode {

    ZOOKEEPER(1, "zookeeper"),
    NACOS(2, "nacos"),


    ;

    private final Integer code;

    private final String name;

}
