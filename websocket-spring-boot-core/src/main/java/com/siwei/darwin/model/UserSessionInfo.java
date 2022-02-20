package com.siwei.darwin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionInfo {

    private String uri;

    private String userId;

    private List<String> instances;

}
