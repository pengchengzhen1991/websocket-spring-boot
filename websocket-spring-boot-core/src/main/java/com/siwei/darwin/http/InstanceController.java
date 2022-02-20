package com.siwei.darwin.http;

import com.siwei.darwin.instance.InstanceClient;
import com.siwei.darwin.instance.RegistryInstance;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(tags = "实例")
@RestController
@RequestMapping(value = "/api/v1/websocket", produces = APPLICATION_JSON_VALUE)
public class InstanceController {
    @Autowired
    private InstanceClient instanceClient;

    @ApiOperation(value = "注册实例")
    @PostMapping("/instance")
    public boolean instance(@RequestBody RegistryInstance instance) {
        return instanceClient.registry(instance);
    }

    @ApiOperation(value = "注册实例")
    @PostMapping("/instance/current")
    public boolean instance() {
        return instanceClient.registry();
    }

    @ApiOperation(value = "查询实例")
    @GetMapping("/instance")
    public RegistryInstance instances(@RequestParam String instanceId) {
        return instanceClient.discover(instanceId);
    }

    @ApiOperation(value = "查询实例")
    @GetMapping("/instances")
    public List<RegistryInstance> instances() {
        return instanceClient.discover();
    }

}
