package com.dragon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Comment extends BaseEntity{


    /**
     * 评论的视频id
     */
    private Integer videoId;

    /**
     * 发送者id
     */
    private Integer userId;

    /**
     * 根节点评论的id,如果为0表示为根节点
     */
    private Integer rootId;

    /**
     * 被回复的评论id，只有root_id为0时才允许为0，表示根评论
     */
    private Integer parentId;

    /**
     * 回复目标用户id
     */
    private Integer toUserId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 逻辑删除 0未删除 1已删除
     */
    private Integer isDeleted;


}
