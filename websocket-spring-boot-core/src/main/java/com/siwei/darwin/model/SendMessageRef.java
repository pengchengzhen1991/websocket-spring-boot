package com.siwei.darwin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SendMessageRef extends SendMessage{

    private Integer status;

    private String describe;

}


