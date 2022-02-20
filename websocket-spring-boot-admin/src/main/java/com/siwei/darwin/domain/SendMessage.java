package com.siwei.darwin.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SendMessage extends BaseEntity {

    private String uri;

    private String userId;

    private String text;

    private Integer status;

    private String describe;

    private String fromInstanceId;

    private String targetInstanceId;

    private Date sendTime;

    private String sessionId;

}
