package com.dragon.service;

import com.dragon.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dragon.vo.VideoHotVo;
import com.dragon.vo.VideoRecentVo;

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
    List<VideoRecentVo> updateByRecent();

    /**
     * 正在热播
     * @return
     */
    List<VideoHotVo> hotBroadcast();
}
