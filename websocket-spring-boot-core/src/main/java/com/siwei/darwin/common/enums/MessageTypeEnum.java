package com.siwei.darwin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageTypeEnum implements ICode {

    SEND(1, "发送"),
    RECEIVE(2, "接受");

    private final Integer code;

    private final String name;

}
