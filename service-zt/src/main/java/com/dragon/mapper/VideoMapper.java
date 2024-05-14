package com.dragon.mapper;

import com.dragon.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dragon.vo.VideoVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 视频表 Mapper 接口
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
public interface VideoMapper extends BaseMapper<Video> {

    /**
     * 动态查询视频数据
     * @param map
     * @return
     */
    List<VideoVo> getVideoByDynamic(Map<String,Object> map);
}
