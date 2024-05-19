package com.dragon.vo;

import lombok.Data;

import java.util.List;

@Data
public class VideoDetailVo {

    /**
     *  视频关联评分信息
     */
    private List<VideoVo> videoWithRating;

    /**
     * 视频类型名称信息
     */
    private List<String> videoTypeList;
}
