package com.siwei.darwin.web.rest;

import com.siwei.darwin.common.api.ApiEntity;
import com.siwei.darwin.domain.SendMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Api(tags = "消息")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin", produces = APPLICATION_JSON_VALUE)
public class SendMessageController {

    @ApiOperation(value = "新增消息")
    @PostMapping("/message")
    public ApiEntity<SendMessage> create(@ApiParam("消息") @Valid @RequestBody SendMessage sendMessage) {

        return ApiEntity.ok();
    }

}
