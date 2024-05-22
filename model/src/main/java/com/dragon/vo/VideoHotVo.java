package com.dragon.vo;

import com.dragon.entity.Type;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VideoHotVo {

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
     * 视频分类名称
     */
    private String categoryName;

    /**
     * 视频观看人数
     */
    private Integer playCount;

    /**
     * 评分
     */
    private Double rating;

    /**
     * 视频类型
     */
    List<String> typeList = new ArrayList<>();
}
