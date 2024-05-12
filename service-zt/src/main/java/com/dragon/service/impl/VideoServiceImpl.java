package com.dragon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragon.constant.MessageConstant;
import com.dragon.entity.Type;
import com.dragon.entity.Video;
import com.dragon.entity.VideoType;
import com.dragon.exception.RuntimeExceptionCustom;
import com.dragon.mapper.VideoMapper;
import com.dragon.service.TypeService;
import com.dragon.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dragon.vo.VideoVo;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.reflect.Typed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.TypedValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * 视频表 服务实现类
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private TypeService typeService;

    @Autowired
    private VideoMapper videoMapper;

    /**
     * 最近更新
     * @return
     */
    @Override
    public List<VideoVo> updateByRecent() {
        while (true){
            LambdaQueryWrapper<Type> queryWrapper = new LambdaQueryWrapper<>();
            //1. 统计类型数量

            //类型条件状态为1
            queryWrapper.eq(Type::getStatus,1);
            int countType = typeService.count(queryWrapper);

            //2. 随机生成1-类型数量的值
            Random random = new Random();
            Integer randomCount = random.nextInt(countType) + 1;

            //3. 根据随机值查询是否存在视频类型
            queryWrapper.eq(Type::getId,randomCount);
            Type type = typeService.getOne(queryWrapper);

            //4. 根据随机类型查询视频数据
            if(type != null){
                //构建查询参数条件
                Map<String, Object> map = new HashMap<>();
                map.put("categoryName","电影");
                map.put("price",1);
                map.put("type",randomCount);
                map.put("status",1);
                map.put("isDeleted",1);
                return videoMapper.getVideoByDynamic(map);
            }
        }
    }
}
