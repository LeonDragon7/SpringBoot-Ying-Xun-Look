package com.dragon.task;

import com.dragon.vo.VideoReRmVo;

import java.util.List;

public interface VideoTask {
    /**
     * 每周更新
     * @return
     */
    List<VideoReRmVo> weekUpdate();
}
