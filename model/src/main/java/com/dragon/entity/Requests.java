package com.dragon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 求片表
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Requests implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 求片ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 求片描述
     */
    private String description;

    /**
     * 求片请求的状态 0待处理 1处理中 2已完成 3已取消
     */
    private Integer status;

    /**
     * 求片请求时间
     */
    private LocalDateTime createTime;


}
