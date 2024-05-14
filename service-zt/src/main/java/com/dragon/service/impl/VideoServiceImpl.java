package com.dragon.service.impl;

import cn.hutool.core.bean.BeanUtil;
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

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 最近更新
     * 条件：
     * - 收费情况：1
     * - 状态：1
     * - 是否被删除(逻辑删除)：1
     * - 根据最近年代获取
     * - 获取前五条数据
     * @return
     */
    @Override
    public List<VideoVo> updateByRecent() {
        //1. 封装查询条件
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getPrice,1)
                .eq(Video::getStatus,1)
                .eq(Video::getIsDeleted,1)
                .orderByDesc(Video::getYear)
                .last("limit 5");
        //2. 查询video表数据
        List<Video> list = list(wrapper);
//        ArrayList<VideoVo> videoVoList = new ArrayList<>();
//        VideoVo videoVo = new VideoVo();
//        for (Video video : list) {
//            BeanUtil.copyProperties(video,videoVo);
//            videoVoList.add(videoVo);
//        }
//        return videoVoList;
        //3. 将数据拷贝到videoVo对象
        List<VideoVo> videoVoList = list.stream().map(v -> {
            VideoVo videoVo = new VideoVo();
            BeanUtil.copyProperties(v, videoVo);
            return videoVo;
        }).collect(Collectors.toList());
        //返回数据
        return videoVoList;
    }
}
