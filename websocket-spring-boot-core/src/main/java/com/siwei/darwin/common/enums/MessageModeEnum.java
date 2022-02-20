package com.siwei.darwin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageModeEnum implements ICode {

    HTTP(1, "http"),
    REDIS(2, "redis"),
    KAFKA(3, "kafka"),

    ;

    private final Integer code;

    private final String name;

}
