package com.siwei.darwin.web.rest;

import com.siwei.darwin.instance.ZookeeperCuratorClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(tags = "工具")
@RestController
@RequestMapping(value = "/api/v1/tool", produces = MediaType.APPLICATION_JSON_VALUE)
public class ToolController {
    @Autowired
    private ZookeeperCuratorClient curatorClient;

    @ApiOperation(value = "删除实例")
    @DeleteMapping("/node")
    public void deleteNode(@RequestParam String path) {
        curatorClient.delete(path);
    }

}
