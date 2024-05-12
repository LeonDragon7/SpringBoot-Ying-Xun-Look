package com.dragon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

    /**
     * 创建用户id
     */
    private Integer createUser;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新用户id
     */
    private Integer updateUser;
}
