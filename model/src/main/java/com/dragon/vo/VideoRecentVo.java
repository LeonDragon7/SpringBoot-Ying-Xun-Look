package com.dragon.vo;

import lombok.Data;

@Data
public class VideoRecentVo {

    private Integer id;
    /**
     * 视频标题
     */
    private String title;

    /**
     * 封面url
     */
    private String coverUrl;
}
