package com.dragon.mapper;

import com.dragon.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dragon.vo.VideoRecentVo;
import org.apache.ibatis.annotations.Select;

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
    List<VideoRecentVo> getVideoByDynamic(Map<String,Object> map);

    /**
     * 查询视频数据带评分名称
     * @return
     */
    @Select("select v1.*,v2.rating from video v1 join video_rate v2 on v1.id = v2.video_id")
    List<Video> selectVideoWithRating();
}
