package com.dragon.controller;


import com.dragon.entity.User;
import com.dragon.result.Result;
import com.dragon.service.VideoService;
import com.dragon.vo.VideoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Api(tags = "首页操作相关接口")
@Slf4j
@RestController
@RequestMapping("/client/index")
public class IndexController {

    @Autowired
    private VideoService videoService;

    /**
     * 最近更新
     * @return
     */
    @ApiOperation("最近更新")
    @GetMapping("/recent")
    public Result<List<VideoVo>> RecentUpdate(){
        List<VideoVo> recentList = videoService.updateByRecent();
        return Result.success(recentList);
    }
}
