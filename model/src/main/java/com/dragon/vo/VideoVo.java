package com.dragon.vo;

import lombok.Data;

import java.time.Year;

@Data
public class VideoVo {

    private Integer id;
    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频的年代
     */
    private Year year;

    /**
     * 视频分类名称
     */
    private String categoryName;

    /**
     * 视频的制作地区
     */
    private String region;

    /**
     * 播放总时长 单位秒
     */
    private Double duration;

    /**
     * 视频语言
     */
    private String language;

    /**
     * 简介
     */
    private String description;

    /**
     * 封面url
     */
    private String coverUrl;

    /**
     * 视频url
     */
    private String videoUrl;

}
