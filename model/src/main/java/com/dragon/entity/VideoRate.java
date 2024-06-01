package com.dragon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.Year;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 视频评分表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VideoRate {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评分用户ID
     */
    private Integer userId;

    /**
     * 视频ID
     */
    private Integer videoId;

    /**
     * 评分
     */
    private Double rating;

    /**
     * 评分人数
     */
    private Long count;

    /**
     * 电影上映时间
     */
    private Year releaseTime;
}
