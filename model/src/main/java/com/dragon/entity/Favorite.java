package com.dragon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 是否删除 0否 1已删除
     */
    private Integer isDelete;


}
