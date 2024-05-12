package com.dragon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 视频类型关联表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VideoType {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 视频ID
     */
    private Integer videoId;

    /**
     * 视频类型ID
     */
    private Integer typeId;


}
