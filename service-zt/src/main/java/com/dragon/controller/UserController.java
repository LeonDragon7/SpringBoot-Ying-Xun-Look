package com.dragon.controller;


import com.dragon.dto.UserLoginDTO;
import com.dragon.result.Result;
import com.dragon.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Api(tags = "用户操作相关接口")
@Slf4j
@RestController
@RequestMapping("/client/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户注册
     * @param userLoginDTO
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody UserLoginDTO userLoginDTO){
        log.info("注册参数：{}",userLoginDTO);
        userService.register(userLoginDTO);
        return Result.success();
    }


    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("登录参数：{}",userLoginDTO);
        Map<String,Object> map = userService.login(userLoginDTO);
        return Result.success(map);
    }

    @ApiOperation("邮箱链接验证")
    @GetMapping("/verifyMail/{key}")
    public Result verifyMail(@PathVariable String key){
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            redisTemplate.delete(key);
            return Result.success();
        }
        return Result.fail();
    }
}

