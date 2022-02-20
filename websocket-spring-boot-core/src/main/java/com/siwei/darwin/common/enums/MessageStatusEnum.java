package com.siwei.darwin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageStatusEnum implements ICode{

    OK(1, "成功"),
    FAIL(2, "失败")

    ;

    private final Integer code;

    private final String name;

}
