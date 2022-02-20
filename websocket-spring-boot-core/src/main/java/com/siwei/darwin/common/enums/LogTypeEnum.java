package com.siwei.darwin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chengzhen
 * @version 1.0
 * @date 2022/2/6 15:43
 */
@Getter
@AllArgsConstructor
public enum LogTypeEnum implements ICode {

    INSTANCE_REGISTRY(1, "实例注册"),
    INSTANCE_CANCEL(2, "实例注销"),
    INSTANCE_EXCEPTION(3, "实例异常"),
    SESSION_REGISTRY(4, "会话注册"),
    SESSION_CANCEL(5, "会话注销"),
    SESSION_EXCEPTION(6, "会话异常"),
    MESSAGE_EXCEPTION(7, "消息异常"),

    ;

    private final Integer code;

    private final String name;

}
