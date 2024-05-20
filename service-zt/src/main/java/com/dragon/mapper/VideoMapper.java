package com.dragon.mapper;

import com.dragon.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dragon.vo.VideoReRmVo;
import com.dragon.vo.VideoVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
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
    List<VideoReRmVo> getVideoByDynamic(Map<String,Object> map);

    /**
     * 查询视频数据带评分名称
     * @return
     */
    @Select("select v1.*,v2.rating from video v1 join video_rate v2 on v1.id = v2.video_id")
    List<VideoVo> selectVideoWithRating();

    /**
     * 随机返回视频数据
     * @param recommendSize
     * @return
     */
    @Select("select * from video where 1=1 limit #{recommendSize}")
    List<Video> findAllByCountLimit(@Param("recommendSize")int recommendSize);

    /**
     * 根据用户标签查询视频
     * @param map
     * @return
     */
    List<Video> getVideoByUserTag(Map<String, Object> map);

    /**
     * 根据id查询视频详细信息
     * @param id
     * @return
     */
    @Select("select v1.title,v1.cover_url,v1.video_url,v1.region,v1.duration,v1.language,v1.description,v1.year,v2.rating from video v1 join video_rate v2 on v1.id = v2.video_id where v1.id = #{videoId}")
    List<VideoVo> getVideoDetail(@Param("videoId") Integer id);


    /**
     * 通过评分由高到低排序
     * 限制五条
     * @return
     */
    @Select("select v1.title,v1.cover_url,v2.rating from video v1 join video_rate v2 on v1.id = v2.video_id where 1=1 order by rating limit 0,5")
    List<VideoReRmVo> getHotRating();
}
