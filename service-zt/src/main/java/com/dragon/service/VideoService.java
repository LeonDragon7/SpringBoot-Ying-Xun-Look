package com.dragon.service;

import com.dragon.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
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
    List<VideoVo> updateByRecent();
}
