package com.siwei.darwin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SessionRegistryTypeEnum implements ICode {

    ZOOKEEPER(1, "zookeeper"),
    HTTP(2, "http")

    ;

    private final Integer code;

    private final String name;

}
