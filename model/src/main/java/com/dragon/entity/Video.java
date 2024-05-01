package com.dragon.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.Year;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 视频表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 上传用户ID
     */
    private Integer userId;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频分类名称
     */
    private String categoryName;

    /**
     * 视频的收费情况 全部 免费 会员 用卷
     */
    private BigDecimal price;

    /**
     * 类型ID
     */
    private Integer type;

    /**
     * 视频的年代
     */
    private Year year;

    /**
     * 视频的制作地区
     */
    private String region;

    /**
     * 播放总时长 单位秒
     */
    private Double duration;

    /**
     * 视频语言
     */
    private String language;

    /**
     * 简介
     */
    private String description;

    /**
     * 封面url
     */
    private String coverUrl;

    /**
     * 视频url
     */
    private String videoUrl;

    /**
     * 状态 0审核中 1已过审 2未通过 3已删除
     */
    private Integer status;

    /**
     * 上传时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新用户ID
     */
    private Integer updateUser;

    /**
     * 删除标记 0不可用 1可用
     */
    private Integer isDeleted;


}
