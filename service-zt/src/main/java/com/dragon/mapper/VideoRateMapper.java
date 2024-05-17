package com.dragon.mapper;

import com.dragon.entity.VideoRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 视频评分表 Mapper 接口
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
public interface VideoRateMapper extends BaseMapper<VideoRate> {

    /**
     * 动态查询用户打过分
     * 分类名称：电影 or 动漫
     * @param userId
     * @return
     */
    List<VideoRate> findAllByUserId(@Param("userId")int userId, @Param("categoryName")String categoryName);
}
