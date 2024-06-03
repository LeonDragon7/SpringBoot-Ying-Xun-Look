package com.dragon.service;

import com.dragon.dto.CommonQueryDTO;
import com.dragon.dto.VideoPageQueryDTO;
import com.dragon.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dragon.result.PageResult;
import com.dragon.vo.VideoDetailVo;
import com.dragon.vo.VideoHotVo;
import com.dragon.vo.VideoReRmVo;

import java.util.List;
import java.util.Map;

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
    List<VideoReRmVo> recommendMovie(Integer userId);

    /**
     * 动漫推荐
     * @return
     */
    List<VideoReRmVo> recommendAnime(Integer userId);

    /**
     * 获取视频详细信息
     * @param id
     * @return
     */
    VideoDetailVo getVideoDetail(Integer id);

    /**
     * 高分影视
     * @return
     */
    Map<String,Object> getHotRating();

    /**
     * 猜你喜欢
     * @return
     */
    List<VideoReRmVo> getLike();

    /**
     * 视频条件分页查询
     * @param videoPageQueryDTO
     * @return
     */
    PageResult pageList(VideoPageQueryDTO videoPageQueryDTO);


    /**
     * 历史观看
     * @param commonQueryDTO
     * @return
     */
    PageResult historyView(CommonQueryDTO commonQueryDTO);

    /**
     * 会员专区
     * @param commonQueryDTO
     * @return
     */
    PageResult prefecture(CommonQueryDTO commonQueryDTO);
}
