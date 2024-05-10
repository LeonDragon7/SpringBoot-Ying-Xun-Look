package com.dragon.vo;

import lombok.Data;

@Data
public class UserVo {
    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;
}
