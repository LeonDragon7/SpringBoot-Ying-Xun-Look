package com.dragon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 收藏表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Favorite extends BaseEntity {


    /**
     * 所属用户ID
     */
    private Integer userId;

    /**
     * 视频ID
     */
    private Integer videoId;

    /**
     * 是否收藏 0不收藏 1收藏
     */
    private Integer visible;

    /**
     * 收藏视频数量
     */
    private Integer count;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 注册用户
     */
    private Integer createUser;


    /**
     * 更新用户
     */
    private Integer updateUser;

    /**
     * 是否删除 0否 1已删除
     */
    private Integer isDelete;


}
