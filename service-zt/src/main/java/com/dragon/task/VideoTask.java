package com.dragon.task;

import com.dragon.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务类 - 影视
 */
@Component
public class VideoTask {
    @Autowired
    private VideoService videoService;
    @Scheduled(cron = "0 0 1 * * 1") // 每周触发一次
    public void WeekUpdate(){
        videoService.weekUpdate();
    }
}
