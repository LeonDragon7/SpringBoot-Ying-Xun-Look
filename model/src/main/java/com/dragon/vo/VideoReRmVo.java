package com.dragon.vo;

import lombok.Data;

import java.time.Year;
import java.util.Date;

@Data
public class VideoReRmVo {

    private Integer id;
    /**
     * 视频标题
     */
    private String title;

    /**
     * 封面url
     */
    private String coverUrl;

    /**
     * 视频的年代
     */
    private Date year;

    /**
     * 评分
     */
    private Double rating;
}
