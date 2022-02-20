package com.siwei.darwin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendResult {

    private Boolean success;

    private String describe;

}
