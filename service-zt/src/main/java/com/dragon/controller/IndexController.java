package com.dragon.controller;

import com.dragon.result.Result;
import com.dragon.service.VideoService;
import com.dragon.vo.VideoHotVo;
import com.dragon.vo.VideoReRmVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.yaml.snakeyaml.nodes.NodeId.sequence;

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
    /**
     * 最近更新
     * @return
     */
    @ApiOperation("最近更新")
    @GetMapping("/recent")
    public Result<List<VideoReRmVo>> RecentUpdate(){
        List<VideoReRmVo> recentList = videoService.updateByRecent();
        return Result.success(recentList);
    }

    /**
     * 正在热播
     * @return
     */
    @ApiOperation("正在热播")
    @GetMapping("/hotBroadcast")
    public Result<List<VideoHotVo>> hotBroadcast(){
        List<VideoHotVo> hotList = videoService.hotBroadcast();
        return Result.success(hotList);
    }

    /**
     * 电影推荐
     * @return
     */
    @ApiOperation("电影推荐")
    @GetMapping("/recommendMovie")
    public Result<List<VideoReRmVo>> recommendMovie(){
        List<VideoReRmVo> videoRecommendList = videoService.recommendMovie();
        return Result.success(videoRecommendList);
    }

    /**
     * 动漫推荐
     * @return
     */
    @ApiOperation("动漫推荐")
    @GetMapping("/recommendAnime")
    public Result<List<VideoReRmVo>> recommendAnime(){
        List<VideoReRmVo> videoRecommendList = videoService.recommendAnime();
        return Result.success(videoRecommendList);
    }

    /**
     * 高分影视
     * @return
     */
    @ApiOperation("高分影视")
    @GetMapping("/hotRating")
    public Result<Map<String,Object>> hotRating(){
        Map<String,Object> hotRatingList = videoService.getHotRating();
        return Result.success(hotRatingList);
    }

    /**
     * 每周更新
     */
    @ApiOperation("每周更新")
    @GetMapping(value = "/weekUpdate")
    public Result<Flux<ServerSentEvent<List<VideoReRmVo>>>> weekUpdate(){
        return Result.success(Flux.interval(Duration.ofDays(7))
                .map(sequence ->{
                    List<VideoReRmVo> weekUpdateList = videoService.weekUpdate();
                    return ServerSentEvent.<List<VideoReRmVo>>builder()
                            .id(String.valueOf(sequence))
                            .event("weekUpdate")
                            .data(weekUpdateList)
                            .build();
                }));
    }
}

