package com.dragon.service;

import com.dragon.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dragon.vo.VideoDetailVo;
import com.dragon.vo.VideoHotVo;
import com.dragon.vo.VideoReRmVo;
import com.dragon.vo.VideoVo;

import java.util.List;

/**
 * <p>
 * 视频表 服务类
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
public interface VideoService extends IService<Video> {

    /**
     * 最近更新
     * @return
     */
    List<VideoReRmVo> updateByRecent();

    /**
     * 正在热播
     * @return
     */
    List<VideoHotVo> hotBroadcast();

    /**
     * 电影推荐
     * @return
     */
    List<VideoReRmVo> recommendMovie();

    /**
     * 动漫推荐
     * @return
     */
    List<VideoReRmVo> recommendAnime();

    /**
     * 获取视频详细信息
     * @param id
     * @return
     */
    VideoDetailVo getVideoDetail(Integer id);
}
