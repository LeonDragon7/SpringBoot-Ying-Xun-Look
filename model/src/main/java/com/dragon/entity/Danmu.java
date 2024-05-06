package com.dragon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 弹幕表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Danmu extends BaseEntity {

    /**
     * 视频ID
     */
    private Integer videoId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 弹幕内容
     */
    private String content;

    /**
     * 字体大小
     */
    private Integer fontsize;

    /**
     * 弹幕模式 1滚动 2顶部 3底部
     */
    private Integer mode;

    /**
     * 弹幕颜色 6位十六进制标准格式
     */
    private String color;

    /**
     * 弹幕所在视频的时间点
     */
    private Double timePoint;

    /**
     * 弹幕状态 1默认过审 2被举报审核中 3删除
     */
    private Integer status;

    /**
     * 发送弹幕的日期时间
     */
    private LocalDateTime createTime;


}
