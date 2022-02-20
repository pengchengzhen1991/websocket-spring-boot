package com.siwei.darwin.http;

import com.siwei.darwin.endpoint.SessionRemoteHandler;
import com.siwei.darwin.model.UserSessionInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(tags = "会话")
@RestController
@RequestMapping(value = "/api/v1/websocket", produces = APPLICATION_JSON_VALUE)
public class SessionController {
    @Autowired
    private SessionRemoteHandler sessionRemoteHandler;

    @ApiOperation(value = "获取用户")
    @GetMapping("/userSessionInfo")
    public List<String> userSessionInfo(@RequestParam String uri,
                                        @RequestParam String userName) {
        return sessionRemoteHandler.getInstances(uri, userName);
    }

    @ApiOperation(value = "获取用户")
    @GetMapping("/userSessionInfoes")
    public List<UserSessionInfo> userSessionInfoes() {
        return sessionRemoteHandler.getUserInstanceInfoes();
    }

}
