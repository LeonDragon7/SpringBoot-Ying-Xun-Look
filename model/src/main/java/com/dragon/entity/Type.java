package com.dragon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 视频类型表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频类型ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 视频类型名称
     */
    private String typeName;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 类型状态 0禁用 1启用
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


}
