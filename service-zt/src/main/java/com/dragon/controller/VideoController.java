package com.dragon.controller;


import com.dragon.annotation.AutoFill;
import com.dragon.result.Result;
import com.dragon.service.VideoService;
import com.dragon.vo.VideoDetailVo;
import com.dragon.vo.VideoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 视频表 前端控制器
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@RestController
@RequestMapping("/client/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 获取视频详细信息
     * @param id
     * @return
     */
    @ApiOperation("根据id获取视频详情信息")
    @GetMapping("/{id}")
    public Result<VideoDetailVo> getById(@PathVariable Integer id){
        VideoDetailVo videoDetail =  videoService.getVideoDetail(id);
        return Result.success(videoDetail);
    }

}

