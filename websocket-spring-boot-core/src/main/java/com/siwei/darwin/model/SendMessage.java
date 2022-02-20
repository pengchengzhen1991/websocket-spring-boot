package com.siwei.darwin.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class SendMessage {

    private Long id;

    private String uri;

    private String userId;

    private String text;

    private String fromInstanceId;

    private String targetInstanceId;

    private Date sendTime;

}
