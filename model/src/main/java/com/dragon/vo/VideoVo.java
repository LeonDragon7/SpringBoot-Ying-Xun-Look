package com.dragon.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class VideoVo {

    private Integer id;
    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频分类名称
     */
    private String categoryName;

    /**
     * 封面url
     */
    private String coverUrl;

    /**
     * 视频url
     */
    private String videoUrl;

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
     * 视频的年代
     */
    private Date year;

    /**
     *  视频评分
     */
    private Double rating;

    /**
     * 播放次数
     */
    private Integer playCount;

    /**
     * 最近播放时间
     */
    private LocalDateTime playTime;
}
