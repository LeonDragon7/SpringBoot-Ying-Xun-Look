package com.dragon.controller;

import com.dragon.dto.CommonQueryDTO;
import com.dragon.result.PageResult;
import com.dragon.result.Result;
import com.dragon.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员专区
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Api(tags = "会员操作相关接口")
@Slf4j
@RestController
@RequestMapping("/client/member")
public class MemberController {
    @Autowired
    private VideoService videoService;

    /**
     * 会员专区
     * @param commonQueryDTO
     * @return
     */
    @ApiOperation("会员专区")
    @GetMapping("/prefecture")
    public Result<PageResult> prefecture(CommonQueryDTO commonQueryDTO){
            PageResult pageResult = videoService.prefecture(commonQueryDTO);
            return Result.success(pageResult);
    }
}
