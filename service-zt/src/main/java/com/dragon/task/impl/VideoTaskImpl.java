package com.dragon.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragon.entity.Type;
import com.dragon.mapper.TypeMapper;
import com.dragon.mapper.VideoMapper;
import com.dragon.service.VideoService;
import com.dragon.task.VideoTask;
import com.dragon.vo.VideoReRmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 定时任务类 - 影视
 */
@Service
public class VideoTaskImpl implements VideoTask{

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private VideoMapper videoMapper;
    /**
     * 每周更新
     * @return
     */
    @Scheduled(cron = "0 0 1 * * 1") // 每周触发一次
    public List<VideoReRmVo> weekUpdate() {
        while (true){
            //1. 统计视频表的类型数量
            LambdaQueryWrapper<Type> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Type::getStatus,1);
            Integer typeCount = typeMapper.selectCount(wrapper);

            //2. 随机生成1-类型数量的值
            Random random = new Random();
            int randomType = random.nextInt(typeCount) + 1;

            //3. 根据随机值查询是否存在视频类型
            wrapper.clear();
            wrapper.eq(Type::getId,randomType);
            Type type = typeMapper.selectOne(wrapper);

            //4. 如果类型存在，则根据随机类型查询视频数据
            if(type != null){
                Map<String, Object> map = new HashMap<>();
                map.put("categoryName", "电影");
                map.put("price", 1);
                map.put("type", 1); // TODO 增加列
                map.put("status", 1);
                map.put("isDeleted", 1);
                return videoMapper.getVideoByDynamic(map);
            }
        }
    }
}
