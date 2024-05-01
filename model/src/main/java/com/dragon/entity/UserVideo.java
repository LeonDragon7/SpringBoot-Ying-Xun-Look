package com.dragon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户视频关联表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 观看视频的用户ID
     */
    private Integer userId;

    /**
     * 视频ID
     */
    private Integer videoId;

    /**
     * 播放次数
     */
    private Integer playCount;

    /**
     * 收藏 0没收藏 1已收藏
     */
    private Integer collect;

    /**
     * 最近播放时间
     */
    private LocalDateTime playTime;

    /**
     * 收藏时间
     */
    private LocalDateTime favoriteTime;


}
