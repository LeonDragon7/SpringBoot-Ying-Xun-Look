package com.dragon.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Date;

@Data
public class VideoPageQueryDTO implements Serializable {

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频分类名称
     */
    private String categoryName;


    /**
     * 视频的收费情况 免费 1 会员 2
     */
    private Integer price;

    /**
     * 类型ID
     */
    private Integer type;

    /**
     * 视频的年代
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date year;

    /**
     * 视频的制作地区
     */
    private String region;

    /**
     * 分页接口标识
     */
    private Integer pageTag;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;


    //    /**
//     * 本周热播 - 当前时间
//     */
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    private LocalDateTime weekTime;
//
//    /**
//     * 历史热播 - 当前时间
//     */
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    private LocalDateTime historyTime;
}
