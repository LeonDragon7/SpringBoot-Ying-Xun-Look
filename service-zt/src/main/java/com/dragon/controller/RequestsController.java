package com.dragon.controller;


import cn.hutool.core.bean.BeanUtil;
import com.dragon.dto.RequestDTO;
import com.dragon.entity.BaseEntity;
import com.dragon.entity.Requests;
import com.dragon.result.Result;
import com.dragon.service.RequestsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 求片表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Api(tags = "求片相关接口")
@Slf4j
@RestController
@RequestMapping("/client/requests")
public class RequestsController {
    @Autowired
    private RequestsService requestsService;

    @ApiOperation("发送求片")
    @PostMapping("/send")
    public Result sendRequests(RequestDTO requestDTO){
        Requests requests = new Requests();
        BeanUtil.copyProperties(requestDTO,requests);
        boolean save = requestsService.save(requests);
        if (!save) return Result.fail("发送失败！");
        return Result.success("发送成功!");
        //TODO 使用Websocket进行前台后台双向通信交互
    }
}

