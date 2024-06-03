package com.dragon.controller;


import com.dragon.dto.CommonQueryDTO;
import com.dragon.dto.VideoPageQueryDTO;
import com.dragon.result.PageResult;
import com.dragon.result.Result;
import com.dragon.service.VideoService;
import com.dragon.vo.VideoDetailVo;
import com.dragon.vo.VideoReRmVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/getById")
    public Result<VideoDetailVo> getById(@RequestParam Integer id){
        VideoDetailVo videoDetail =  videoService.getVideoDetail(id);
        return Result.success(videoDetail);
    }

    /**
     * 猜你喜欢
     * @return
     */
    @ApiOperation("猜你喜欢")
    @GetMapping("/like")
    public Result<List<VideoReRmVo>> like(@RequestParam Integer userId){
        List<VideoReRmVo> videoLikeList = videoService.getLike(userId);
        return Result.success(videoLikeList);
    }

    /**
     * 条件分页查询
     * @param videoPageQueryDTO
     * @return
     */
    @ApiOperation("视频条件分页")
    @GetMapping("/pageQuery")
    public Result<PageResult> pageQuery(VideoPageQueryDTO videoPageQueryDTO){
        PageResult pageResult = videoService.pageList(videoPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 历史观看
     * @param commonQueryDTO
     * @return
     */
    @ApiOperation("历史观看")
    @GetMapping("/historyView")
    public Result<PageResult> historyView(CommonQueryDTO commonQueryDTO){
        PageResult pageResult = videoService.historyView(commonQueryDTO);
        return Result.success(pageResult);
    }
}

